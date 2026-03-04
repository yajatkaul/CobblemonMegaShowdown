package com.github.yajatkaul.mega_showdown.client.battle.hud;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattle;
import com.cobblemon.mod.common.client.battle.ClientBattleActor;
import com.cobblemon.mod.common.client.battle.ClientBattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import com.cobblemon.mod.common.pokemon.status.PersistentStatusContainer;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattlePokemonState;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattleSideState;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattleStatePacketS2C;
import com.github.yajatkaul.mega_showdown.client.battle.storage.BattlePokemonMemory;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BattleHud {
    private static int prevTime = 0;
    private static final Map<UUID, BattlePokemonMemory> memory = new HashMap<>();
    private static final List<TeamPreviewWidget> teamPreviews = List.of(new TeamPreviewWidget(0, 0, true), new TeamPreviewWidget(0, 0, false));
    private static final List<UUID> pokemonAtTurnStart = new ArrayList<>();
    private static final List<Component> messagesThisTurn = new ArrayList<>();

    // Item Translation Key Handling

    /**
     * Arg1 is the user, Arg2 is the item
     */
    private static final Set<String> twoArgItemText = Set.of(
        "cobblemon.battle.item.airballoon",
        "cobblemon.battle.item.recycle",
        "cobblemon.battle.item.harvest",
        "cobblemon.battle.item.frisk",
        "cobblemon.battle.item.bestow",
        "cobblemon.battle.heal.leftovers",
        "cobblemon.battle.heal.item",
        "cobblemon.battle.damage.item"
    );

    public static void receivePacket (BattleStatePacketS2C packet, NetworkManager.PacketContext context) {
        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (CobblemonClient.INSTANCE.getBattle() == null || Minecraft.getInstance().player == null) return;

        for (BattleSideState side : packet.sides()) {
            for (BattlePokemonState pokemon : side.getPokemon()) {
                memory.putIfAbsent(pokemon.uuid(), new BattlePokemonMemory(pokemon.uuid()));
                memory.get(pokemon.uuid()).setState(pokemon);
            }
        }

        int pokemonCount = packet.sides().stream().mapToInt(side -> side.getPokemon().size()).sum();
        int widgetCount = teamPreviews.stream().mapToInt(TeamPreviewWidget::getPartySize).sum();
        if (widgetCount != pokemonCount) {
            teamPreviews.forEach(TeamPreviewWidget::clearParty);

            packet.sides().forEach(side -> {
                Optional<ClientBattleActor> anyActor = side.actors()
                    .stream()
                    .map(actor -> battle.getParticipatingActor(actor.uuid()))
                    .filter(Objects::nonNull)
                    .findAny();

                if (anyActor.isEmpty()) return; // This should not happen!
                boolean isLeft = anyActor.get().getSide().equals(battle.getSide1());

                for (BattlePokemonState battlePokemonState : side.getPokemon()) {
                    BattlePokemonMemory pokemon = memory.get(battlePokemonState.uuid());
                    if (pokemon == null) continue; // Should never happen, but just in case

                    TeamPreviewWidget widget = isLeft ? teamPreviews.getFirst() : teamPreviews.getLast();
                    widget.addPartyMember(new PokeballPreviewWidget(isLeft, pokemon));
                }
            });
        }
    }

    public static void tickLocalBattleInfo () {
        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (CobblemonClient.INSTANCE.getBattle() == null || Minecraft.getInstance().player == null) return;

        for (Map.Entry<UUID, BattlePokemonMemory> entry : memory.entrySet()) {
            entry.getValue().setActive(false);
        }

        for (ClientBattleSide side : battle.getSides()) {
            for (ActiveClientBattlePokemon pokemon : side.getActiveClientBattlePokemon()) {
                if (pokemon.getBattlePokemon() == null) continue;

                BattlePokemonMemory pokemonMemory = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                pokemonMemory.setRenderablePokemon(pokemon.getBattlePokemon().getProperties().asRenderablePokemon());
                pokemonMemory.setName(pokemon.getBattlePokemon().getDisplayName().getString());
                pokemonMemory.setOwner(pokemon.getActor().getDisplayName().getString());
                pokemonMemory.setActive(true);

                if (pokemon.getBattlePokemon().isHpFlat()) {
                    pokemonMemory.setHealthPercentage(pokemon.getBattlePokemon().getHpValue() / pokemon.getBattlePokemon().getMaxHp());
                }
                else {
                    pokemonMemory.setHealthPercentage(pokemon.getBattlePokemon().getHpValue());
                }

                PersistentStatus status = pokemon.getBattlePokemon().getStatus();
                if (status == null) pokemonMemory.setStatus(null);
                else pokemonMemory.setStatus(status.getShowdownName());

                if (teamPreviews.stream().noneMatch(widget -> widget.hasPartyMember(pokemon.getBattlePokemon().getUuid()))) {
                    boolean isLeft;

                    if (battle.getSpectating()) isLeft = side.equals(battle.getSide2());
                    else isLeft = side.equals(battle.getSide1());

                    TeamPreviewWidget widget = isLeft ? teamPreviews.getFirst() : teamPreviews.getLast();
                    widget.addPartyMember(new PokeballPreviewWidget(isLeft, pokemonMemory));
                }
            }
        }

        // If you're in the battle, then you have full information for your own party.
        if (!battle.getSpectating()) {
            ClientBattleActor myActor = battle.getParticipatingActor(Minecraft.getInstance().player.getUUID());
            if (myActor != null) {
                for (Pokemon pokemon : myActor.getPokemon()) {
                    BattlePokemonMemory pokemonMemory = memory.computeIfAbsent(pokemon.getUuid(), BattlePokemonMemory::new);
                    pokemonMemory.setRenderablePokemon(pokemon.asRenderablePokemon());
                    pokemonMemory.setMoves(pokemon.getMoveSet());
                    pokemonMemory.setAbility(pokemon.getAbility().getName());

                    if (pokemon.heldItem().isEmpty()) {
                        pokemonMemory.setItem("");
                    }
                    else if (pokemonMemory.getItem() == null) { // Too many weird edge cases involving items, the packets will always be correct so skip this if its already been done.
                        pokemonMemory.setItem(pokemon.heldItem().getDescriptionId());
                    }

                    PersistentStatusContainer status = pokemon.getStatus();
                    if (status == null) pokemonMemory.setStatus(null);
                    else pokemonMemory.setStatus(status.getStatus().getShowdownName());

                    if (!teamPreviews.getFirst().hasPartyMember(pokemon.getUuid())) teamPreviews.getFirst().addPartyMember(new PokeballPreviewWidget(true, pokemonMemory));
                }
            }
        }
    }

    public static void messageCallback (List<Component> messages) {
        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (battle == null) return;

        for (Component message : messages) {
            messagesThisTurn.add(message);

            if (message.getContents() instanceof TranslatableContents content) {
                if (content.getKey().contains("cobblemon.battle.used_move") && content.getArgs().length >= 2) {
                    Object userArg = content.getArgs()[0];
                    Object moveArg = content.getArgs()[1];
                    if (userArg instanceof Component userText
                        && userText.getContents() instanceof TranslatableContents
                        && moveArg instanceof Component moveText
                        && moveText.getContents() instanceof TranslatableContents argContent
                    ) {
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(userArg);
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon == null || pokemon.getBattlePokemon() == null) continue;

                        BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                        mem.useMove(argContent.getKey().replace("cobblemon.move.", ""));
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.ability.") && content.getArgs().length >= 1) {
                    OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[0]);

                    if (content.getKey().contains("generic") && content.getArgs().length >= 2) {
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.confirmNoIllusion();
                            if (mem.getAbility() == null && !mem.isTransformed()) {
                                mem.setAbility(content.getArgs()[1].toString());
                            }
                        }
                    }
                    else if (content.getKey().contains("trace") && content.getArgs().length >= 3) {
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.setTempAbility(content.getArgs()[2].toString());
                        }

                        ActiveClientBattlePokemon target = getPokemon(battle, OwnedPokemon.fromTextArg(content.getArgs()[1]));
                        if (target != null && target.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(target.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            if (mem.getAbility() != null && !mem.isTransformed()) {
                                mem.setAbility(content.getArgs()[2].toString());
                            }
                        }
                    }
                    else if ((content.getKey().contains("replace") || content.getKey().contains("receiver")) && content.getArgs().length >= 2) {
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.setTempAbility(content.getArgs()[1].toString());
                        }
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.enditem.") && content.getArgs().length > 0) {
                    OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                    ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                    if (pokemon != null && pokemon.getBattlePokemon() != null) {
                        BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                        mem.setConsumedItem(true);
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.transform") && content.getArgs().length > 0) {
                    OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                    ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                    if (pokemon != null && pokemon.getBattlePokemon() != null) {
                        BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                        mem.setTransformed(true);
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.end.illusion")) { // I hate Zoroark, omg
                    int indexOfMoveOrEffectiveness = messagesThisTurn.size() - 2;
                    int indexOfMove = messagesThisTurn.size() - 3;
                    if (indexOfMoveOrEffectiveness >= 0
                        && messagesThisTurn.get(indexOfMoveOrEffectiveness).getContents() instanceof TranslatableContents content2
                        && content2.getKey().contains("cobblemon.battle.used_move_on")
                    ) {
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content2.getArgs()[2]);
                        BattlePokemonMemory mem = getMemory(owned);
                        if (mem != null) {
                            OwnedPokemon zoroark = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                            BattlePokemonMemory zoroarkMem = getMemory(zoroark);
                            if (zoroarkMem != null) mem.transferIllusoryDataTo(zoroarkMem);

                            mem.clearIllusoryData();
                        }
                    }
                    else if (indexOfMove >= 0
                        && messagesThisTurn.get(indexOfMove).getContents() instanceof TranslatableContents content2
                        && content2.getKey().contains("cobblemon.battle.used_move_on")
                    ) {
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content2.getArgs()[2]);
                        BattlePokemonMemory mem = getMemory(owned);
                        if (mem != null) {
                            OwnedPokemon zoroark = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                            BattlePokemonMemory zoroarkMem = getMemory(zoroark);
                            if (zoroarkMem != null) mem.transferIllusoryDataTo(zoroarkMem);

                            mem.clearIllusoryData();
                        }
                    }
                    else {
                        Optional<UUID> inactiveUUID = getAllActivePokemon(battle)
                            .stream()
                            .filter(p -> p.getBattlePokemon() != null)
                            .map(p -> p.getBattlePokemon().getUuid())
                            .filter(uuid -> pokemonAtTurnStart.stream().noneMatch(pokemon -> pokemon.equals(uuid)))
                            .findAny();

                        if (inactiveUUID.isPresent()) {
                            BattlePokemonMemory mem = memory.get(inactiveUUID.get());
                            mem.clearIllusoryData();
                        }
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.turn")) {
                    List<ActiveClientBattlePokemon> current = getAllActivePokemon(battle);
                    List<UUID> removed = pokemonAtTurnStart
                        .stream()
                        .filter(uuid -> current.stream().noneMatch(pokemon -> pokemon.getBattlePokemon() != null && pokemon.getBattlePokemon().getUuid().equals(uuid)))
                        .toList();

                    List<UUID> sentOut = current
                        .stream()
                        .filter(pokemon -> pokemon.getBattlePokemon() != null)
                        .map(pokemon -> pokemon.getBattlePokemon().getUuid())
                        .filter(uuid -> pokemonAtTurnStart.stream().noneMatch(uuid1 -> uuid1.equals(uuid)))
                        .toList();

                    pokemonAtTurnStart.clear();
                    for (ActiveClientBattlePokemon pokemon : current) {
                        if (pokemon.getBattlePokemon() != null) pokemonAtTurnStart.add(pokemon.getBattlePokemon().getUuid());
                    }

                    for (UUID pokemon : sentOut) {
                        BattlePokemonMemory mem = memory.computeIfAbsent(pokemon, BattlePokemonMemory::new);
                        mem.onSendOut();
                    }

                    for (UUID pokemon : removed) {
                        BattlePokemonMemory mem = memory.computeIfAbsent(pokemon, BattlePokemonMemory::new);
                        mem.onRemovedFromField();
                    }

                    for (int i = 0; i < messagesThisTurn.size(); ++i) {
                        Component text = messagesThisTurn.get(i);
                        if (text.getContents() instanceof TranslatableContents content2 && content2.getKey().contains("cobblemon.battle.used_move_on")) {
                            String nextKey = null;
                            if (i < messagesThisTurn.size() - 1) {
                                Component nextText = messagesThisTurn.get(i + 1);
                                if (nextText.getContents() instanceof TranslatableContents nextContent) {
                                    nextKey = nextContent.getKey();
                                }
                            }

                            if (nextKey != null && !nextKey.contains("cobblemon.battle.immune") && !nextKey.contains("cobblemon.battle.missed") && !nextKey.contains("cobblemon.battle.end.illusion")) {
                                // The pokemon maybe took an attack
                                if (content2.getArgs()[1] instanceof Component moveText && moveText.getContents() instanceof TranslatableContents moveContent) {
                                    String moveName = moveContent.getKey().replace("cobblemon.move.", "");
                                    if (Moves.getByNameOrDummy(moveName).getDamageCategory() != DamageCategories.INSTANCE.getSTATUS()) {
                                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content2.getArgs()[2]);
                                        BattlePokemonMemory mem = getOrCreateMemory(getPokemon(battle, owned));
                                        if (mem != null) mem.confirmNoIllusion();
                                    }
                                }
                            }
                        }
                    }
                    messagesThisTurn.clear();
                }
                else if ((content.getKey().contains("cobblemon.battle.boost.") || content.getKey().contains("cobblemon.battle.unboost.")) && content.getArgs().length >= 2) {
                    if (content.getArgs()[1] instanceof Component statText && statText.getContents() instanceof TranslatableContents statContent) {
                        OwnedPokemon pokemon = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                        BattlePokemonMemory mem = getMemory(pokemon);
                        if (mem == null) continue;

                        String key = statContent.getKey().replace("cobblemon.stat.", "").replace(".name", "");
                        key = switch (key) {
                            case "attack" -> "atk";
                            case "defence" -> "def";
                            case "special_attack" -> "spa";
                            case "special_defence" -> "spd";
                            case "speed" -> "spe";
                            case "accuracy" -> "acc";
                            case "evasion" -> "eva";
                            default -> key;
                        };

                        int multiplier = content.getKey().contains("unboost") ? -1 : 1;
                        int amount = 0;
                        if (content.getKey().contains("slight")) amount = 1;
                        else if (content.getKey().contains("sharp")) amount = 2;
                        else if (content.getKey().contains("severe")) amount = 3;

                        if (amount > 0) {
                            mem.getStatChanges().put(key, mem.getStatChanges().getOrDefault(key, 0) + amount * multiplier);
                        }
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.clearallboost")) {
                    memory.values().forEach(mem -> mem.getStatChanges().clear());
                }
                else if (content.getKey().contains("cobblemon.battle.clearallnegativeboost") && content.getArgs().length > 0) {
                    OwnedPokemon pokemon = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                    BattlePokemonMemory mem = getMemory(pokemon);
                    if (mem != null) {
                        mem.getStatChanges().replaceAll((key, value) -> value < 0 ? 0 : value);
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.clearboost") && content.getArgs().length > 0) {
                    OwnedPokemon pokemon = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                    BattlePokemonMemory mem = getMemory(pokemon);
                    if (mem != null) {
                        mem.getStatChanges().clear();
                    }
                }
                else if (content.getKey().contains("cobblemon.battle.setboost.") && content.getArgs().length > 0) {
                    OwnedPokemon pokemon = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                    BattlePokemonMemory mem = getMemory(pokemon);
                    if (mem == null) continue;

                    if (content.getKey().contains("bellydrum") || content.getKey().contains("angerpoint")) {
                        mem.getStatChanges().put("atk", 6);
                    }
                }
                else { // There is nothing consistent for items
                    if (twoArgItemText.contains(content.getKey()) && content.getArgs().length >= 2) {
                        String item;
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[0]);

                        if (content.getArgs()[1] instanceof Component itemText) {
                            if (itemText.getContents() instanceof TranslatableContents itemContent) {
                                item = itemContent.getKey();
                            }
                            else {
                                item = itemText.getString();
                            }
                        }
                        else item = content.getArgs()[1].toString();

                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.setItem(item);
                        }
                    }
                    else if (content.getKey().equalsIgnoreCase("cobblemon.battle.damage.rockyhelmet") && content.getArgs().length >= 2) {
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[1]);
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.setItem(CobblemonItems.ROCKY_HELMET);
                        }
                    }
                    else if (content.getKey().equalsIgnoreCase("cobblemon.battle.damage.lifeorb") && content.getArgs().length >= 1) {
                        OwnedPokemon owned = OwnedPokemon.fromTextArg(content.getArgs()[0]);
                        ActiveClientBattlePokemon pokemon = getPokemon(battle, owned);
                        if (pokemon != null && pokemon.getBattlePokemon() != null) {
                            BattlePokemonMemory mem = memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
                            mem.setItem(CobblemonItems.LIFE_ORB);
                        }
                    }
                }
            }
        }
    }

    public static void hudCallback (GuiGraphics context, DeltaTracker counter) {
        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (battle == null) {
            memory.clear();
            messagesThisTurn.clear();
            pokemonAtTurnStart.clear();
            teamPreviews.forEach(TeamPreviewWidget::clearParty);
            return;
        }

        if (Minecraft.getInstance().player != null && Minecraft.renderNames()) {
            int time = Minecraft.getInstance().player.tickCount;
            if (time != prevTime) {
                tickLocalBattleInfo();
                prevTime = time;
            }

            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int mouseX = (int)(Minecraft.getInstance().mouseHandler.xpos() * width / Minecraft.getInstance().getWindow().getWidth());
            int mouseY = (int)(Minecraft.getInstance().mouseHandler.ypos() * height / Minecraft.getInstance().getWindow().getHeight());
            float tickDelta = counter.getGameTimeDeltaPartialTick(true);

            for (TeamPreviewWidget widget : teamPreviews) {
                widget.realignToScreen();
                widget.render(context, mouseX, mouseY, tickDelta);
            }
        }
    }

    public static void drawStatChanges (GuiGraphics context, ActiveClientBattlePokemon pokemon, boolean isLeft, int rank, boolean isCompact) {
        BattlePokemonMemory mem = getMemory(pokemon);
        if (mem == null) return;

        StatChangeRenderer.render(context, mem, isLeft, rank, isCompact);
    }

    @Nullable
    private static ActiveClientBattlePokemon getPokemon (ClientBattle battle, OwnedPokemon pokemon) {
        return getPokemon(battle, pokemon.pokemon, pokemon.owner);
    }

    @Nullable
    private static ActiveClientBattlePokemon getPokemon (ClientBattle battle, String name, String owner) {
        for (ClientBattleSide side : battle.getSides()) {
            for (ActiveClientBattlePokemon pokemon : side.getActiveClientBattlePokemon()) {
                if (pokemon.getBattlePokemon() == null) continue;

                if (pokemon.getBattlePokemon().getDisplayName().getString().equals(name)
                    && pokemon.getActor().getDisplayName().getString().equals(owner)
                ) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    private static List<ActiveClientBattlePokemon> getAllActivePokemon (ClientBattle battle) {
        List<ActiveClientBattlePokemon> list = new ArrayList<>();
        for (ClientBattleSide side : battle.getSides()) {
            for (ActiveClientBattlePokemon pokemon : side.getActiveClientBattlePokemon()) {
                if (pokemon.getBattlePokemon() == null) continue;

                list.add(pokemon);
            }
        }
        return list;
    }

    @Nullable
    private static BattlePokemonMemory getOrCreateMemory (@Nullable ActiveClientBattlePokemon pokemon) {
        if (pokemon != null && pokemon.getBattlePokemon() != null) {
            return memory.computeIfAbsent(pokemon.getBattlePokemon().getUuid(), BattlePokemonMemory::new);
        }
        return null;
    }

    @Nullable
    private static BattlePokemonMemory getMemory (@Nullable ActiveClientBattlePokemon pokemon) {
        if (pokemon != null && pokemon.getBattlePokemon() != null) {
            return memory.get(pokemon.getBattlePokemon().getUuid());
        }
        return null;
    }

    @Nullable
    private static BattlePokemonMemory getMemory (OwnedPokemon pokemon) {
        for (BattlePokemonMemory mem : memory.values()) {
            if (pokemon.owner().equals(mem.getOwner()) && pokemon.pokemon().equals(mem.getName())) return mem;
        }
        return null;
    }

    private record OwnedPokemon (String pokemon, String owner) {
        private static OwnedPokemon fromTextArg (Object arg) {
            String pokemon;
            String owner;

            if (arg instanceof Component text) {
                if (text.getContents() instanceof TranslatableContents content && content.getArgs().length >= 2) {
                    if (content.getArgs()[0] instanceof Component ownerText) owner = ownerText.getString();
                    else owner = content.getArgs()[0].toString();

                    if (content.getArgs()[1] instanceof Component pokemonText) pokemon = pokemonText.getString();
                    else pokemon = content.getArgs()[1].toString();
                }
                else {
                    pokemon = text.getString();
                    owner = text.getString();
                }
            }
            else {
                pokemon = arg.toString();
                owner = arg.toString();
            }

            return new OwnedPokemon(pokemon, owner);
        }
    }
}
