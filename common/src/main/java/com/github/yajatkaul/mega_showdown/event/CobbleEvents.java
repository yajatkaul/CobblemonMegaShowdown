package com.github.yajatkaul.mega_showdown.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent;
import com.cobblemon.mod.common.api.events.battles.instruction.FormeChangeEvent;
import com.cobblemon.mod.common.api.events.battles.instruction.MegaEvolutionEvent;
import com.cobblemon.mod.common.api.events.battles.instruction.TerastallizationEvent;
import com.cobblemon.mod.common.api.events.battles.instruction.ZMoveUsedEvent;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent;
import com.cobblemon.mod.common.api.events.pokemon.HeldItemEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonSentEvent;
import com.cobblemon.mod.common.api.events.pokemon.RidePokemonEvent;
import com.cobblemon.mod.common.api.events.pokemon.healing.PokemonHealedEvent;
import com.cobblemon.mod.common.api.item.HealingSource;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import com.cobblemon.mod.common.battles.dispatch.UntilDispatch;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.properties.AspectPropertyType;
import com.cobblemon.mod.common.pokemon.properties.UnaspectPropertyType;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.api.event.DynamaxEndCallback;
import com.github.yajatkaul.mega_showdown.api.event.DynamaxStartCallback;
import com.github.yajatkaul.mega_showdown.api.event.UltraBurstCallback;
import com.github.yajatkaul.mega_showdown.codec.BattleFormChange;
import com.github.yajatkaul.mega_showdown.codec.Effect;
import com.github.yajatkaul.mega_showdown.codec.HeldItemFormChange;
import com.github.yajatkaul.mega_showdown.codec.ZCrystal;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;
import com.github.yajatkaul.mega_showdown.gimmick.MaxGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.UltraGimmick;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.FormChangeHeldItem;
import com.github.yajatkaul.mega_showdown.sound.MegaShowdownSounds;
import com.github.yajatkaul.mega_showdown.tag.MegaShowdownTags;
import com.github.yajatkaul.mega_showdown.utils.*;
import kotlin.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class CobbleEvents {
    public static void register() {
        CobblemonEvents.HELD_ITEM_PRE.subscribe(Priority.NORMAL, CobbleEvents::heldItemChange);
        CobblemonEvents.MEGA_EVOLUTION.subscribe(Priority.NORMAL, CobbleEvents::megaEvolution);
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, CobbleEvents::hookBattlePre);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, CobbleEvents::hookBattleStarted);
        CobblemonEvents.TERASTALLIZATION.subscribe(Priority.NORMAL, CobbleEvents::terrastallizationUsed);
        CobblemonEvents.POKEMON_HEALED.subscribe(Priority.NORMAL, CobbleEvents::healedPokemons);
        CobblemonEvents.ZPOWER_USED.subscribe(Priority.NORMAL, CobbleEvents::zMovesUsed);
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, CobbleEvents::devolveFainted);
        CobblemonEvents.POKEMON_SENT_POST.subscribe(Priority.NORMAL, CobbleEvents::pokemonSent);
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(Priority.NORMAL, CobbleEvents::pokeballHit);
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, CobbleEvents::fixTera);
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.NORMAL, CobbleEvents::dropShardPokemon);
        CobblemonEvents.FORME_CHANGE.subscribe(Priority.NORMAL, CobbleEvents::formChanged);
        CobblemonEvents.RIDE_EVENT_PRE.subscribe(Priority.NORMAL, CobbleEvents::ridePre);

        DynamaxStartCallback.EVENT.register(CobbleEvents::dynamaxStarted);
        DynamaxEndCallback.EVENT.register(CobbleEvents::dynamaxEnded);
        UltraBurstCallback.EVENT.register(CobbleEvents::ultraBurst);
    }

    private static void ridePre(RidePokemonEvent.Pre event) {
        if (event.getPokemon().getPokemon().getPersistentData().getBoolean("form_changing")) {
            event.cancel();
        }
    }

    private static void formChanged(FormeChangeEvent formeChangeEvent) {
        if (formeChangeEvent.getFormeName().equals("x") || formeChangeEvent.getFormeName().equals("y")
                || formeChangeEvent.getFormeName().equals("mega")) {
            return;
        }
        BattlePokemon battlePokemon = formeChangeEvent.getPokemon();
        Pokemon pokemon = formeChangeEvent.getPokemon().getEffectedPokemon();

        for (BattleFormChange battleFormChange : MegaShowdownDatapackRegister.BATTLE_FORM_CHANGE_REGISTRY) {
            if (formeChangeEvent.getFormeName().equals(battleFormChange.showdownFormChangeId())
                    && battleFormChange.pokemons().contains(pokemon.getSpecies().getName())
                    && battleFormChange.aspects().validate_apply(pokemon)) {
                Effect.getEffect(battleFormChange.effect()).applyEffectsBattle(pokemon,
                        battleFormChange.aspects().apply_aspects(),
                        null,
                        battlePokemon
                );

                AspectUtils.appendRevertDataPokemon(
                        Effect.getEffect(battleFormChange.effect()),
                        battleFormChange.aspects().revert_aspects(),
                        pokemon,
                        "battle_end_revert"
                );
                return;
            }
        }
    }

    private static void dropShardPokemon(LootDroppedEvent event) {
        if (!MegaShowdownConfig.teralization) {
            return;
        }

        if (event.getEntity() instanceof PokemonEntity pokemonEntity) {
            Pokemon pokemon = pokemonEntity.getPokemon();
            Item correspondingTeraShard = TeraHelper.getTeraShardForType(pokemon.getTypes());
            ItemDropEntry teraShardDropEntry = new ItemDropEntry();
            teraShardDropEntry.setItem(BuiltInRegistries.ITEM.getKey(correspondingTeraShard));

            Random random = new Random();

            boolean otherSuccess = random.nextDouble() < (MegaShowdownConfig.teraShardDropRate / 100.0);
            boolean stellarSuccess = random.nextDouble() < (MegaShowdownConfig.stellarShardDropRate / 100.0);

            if (otherSuccess) {
                event.getDrops().add(teraShardDropEntry);
            } else if (stellarSuccess) {
                teraShardDropEntry.setItem(BuiltInRegistries.ITEM.getKey(MegaShowdownItems.STELLAR_TERA_SHARD.get()));
                event.getDrops().add(teraShardDropEntry);
            }
        }
    }

    private static void fixTera(PokemonCapturedEvent event) {
        Pokemon pokemon = event.getPokemon();

        if (pokemon.getSpecies().getName().equals("Ogerpon")) {
            if (pokemon.getAspects().contains("teal-mask")) {
                pokemon.setTeraType(TeraTypes.getGRASS());
            } else if (pokemon.getAspects().contains("wellspring-mask")) {
                pokemon.setTeraType(TeraTypes.getWATER());
            } else if (pokemon.getAspects().contains("hearthflame-mask")) {
                pokemon.setTeraType(TeraTypes.getFIRE());
            } else if (pokemon.getAspects().contains("cornerstone-mask")) {
                pokemon.setTeraType(TeraTypes.getROCK());
            } else {
                pokemon.setTeraType(TeraHelper.getTeraFromElement(pokemon.getPrimaryType()));
            }
        } else if (pokemon.getSpecies().getName().equals("Terapagos")) {
            pokemon.setTeraType(TeraTypes.getSTELLAR());
        }
    }

    private static void pokeballHit(ThrownPokeballHitEvent event) {
        if (event.getPokemon().getAspects().contains("core-percent")) {
            event.cancel();
        }
    }

    private static void pokemonSent(PokemonSentEvent.Post event) {
        PokemonEntity pokemon = event.getPokemonEntity();

        if (pokemon.getPokemon().getPersistentData().getBoolean("is_tera") && MegaShowdownConfig.legacyTeraEffect) {
            GlowHandler.applyTeraGlow(pokemon);
        }
    }

    private static void devolveFainted(BattleFaintedEvent event) {
        Pokemon pokemon = event.getKilled().getEffectedPokemon();

        if (pokemon.getPersistentData().contains("battle_end_revert")) {
            List<AspectUtils.EffectPair> effects = AspectUtils.getRevertDataPokemon(pokemon, "battle_end_revert");

            for (AspectUtils.EffectPair effectPair : effects) {
                effectPair.effect().revertEffects(pokemon, effectPair.aspects(), null);
            }
            pokemon.getPersistentData().remove("battle_end_revert");
        }

        if (pokemon.getPersistentData().getBoolean("is_tera")) {
            pokemon.getAspects().stream().filter(a -> a.startsWith("msd:tera_")).forEach(name -> {
                UnaspectPropertyType.INSTANCE.fromString("msd:tera_" + pokemon.getTeraType().showdownId()).apply(pokemon);
            });
            pokemon.getPersistentData().remove("is_tera");
            if (pokemon.getEntity() != null) {
                if (MegaShowdownConfig.legacyTeraEffect) {
                    pokemon.getEntity().removeEffect(MobEffects.GLOWING);
                }
            }
        }

        if (pokemon.getPersistentData().getBoolean("is_max")) {
            pokemon.getPersistentData().putBoolean("is_max", false);
        }
    }

    private static void ultraBurst(PokemonBattle battle, BattlePokemon battlePokemon) {
        Pokemon pokemon = battlePokemon.getEntity().getPokemon();
        UltraGimmick.ultraBurstInBattle(pokemon, battlePokemon);
    }

    private static void dynamaxEnded(PokemonBattle battle, BattlePokemon battlePokemon) {
        Pokemon pokemon = battlePokemon.getEffectedPokemon();
        MaxGimmick.startGradualScalingDown(pokemon.getEntity());

        if (battlePokemon.getEntity().getPokemon().getAspects().contains("gmax")) {
            Effect.getEffect("mega_showdown:dynamax_end").applyEffectsBattle(pokemon, List.of("dynamax_form=none"), null, battlePokemon);
        }
    }

    private static void dynamaxStarted(PokemonBattle battle, BattlePokemon battlePokemon, Boolean gmax) {
        Pokemon pokemon = battlePokemon.getEffectedPokemon();

        if (gmax) {
            Effect.getEffect("mega_showdown:dynamax_start").applyEffectsBattle(pokemon, List.of("dynamax_form=gmax"), null, battlePokemon);
            AspectUtils.appendRevertDataPokemon(
                    Effect.empty(),
                    List.of("dynamax_form=none"),
                    pokemon,
                    "battle_end_revert"
            );

            battle.dispatchToFront(() -> new UntilDispatch(() -> true));
        } else {
            Effect.getEffect("mega_showdown:dynamax_start").applyEffectsBattle(pokemon, List.of(), null, battlePokemon);
        }

        pokemon.getPersistentData().putBoolean("is_max", true);

        PokemonEntity pokemonEntity = pokemon.getEntity();

        if (pokemonEntity != null) {
            MaxGimmick.startGradualScaling(pokemon.getEntity(), MegaShowdownConfig.dynamaxScaleFactor);

            BlockPos entityPos = pokemon.getEntity().getOnPos();
            pokemonEntity.level().playSound(
                    null, entityPos.getX(), entityPos.getY(), entityPos.getZ(),
                    MegaShowdownSounds.DYNAMAX.get(),
                    SoundSource.PLAYERS, 0.2f, 0.8f
            );
        }
    }

    private static void zMovesUsed(ZMoveUsedEvent event) {
        PokemonEntity pokemonEntity = event.getPokemon().getEffectedPokemon().getEntity();
        Pokemon pokemon = pokemonEntity.getPokemon();

        AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "z/z_moves");

        if (pokemon.getSpecies().getName().equals("Pikachu") && pokemon.getAspects().contains("partner-cap")) {
            AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "bond/ash_pikachu");
        }

        GlowHandler.applyZGlow(pokemonEntity, RegistryLocator.getComponent(ZCrystal.class, pokemon.heldItem()));

        Effect.getEffect("mega_showdown:z_move").applyEffectsBattle(pokemon, List.of(), null, event.getPokemon());
    }

    private static void healedPokemons(PokemonHealedEvent pokemonHealedEvent) {
        ServerPlayer player = pokemonHealedEvent.getPokemon().getOwnerPlayer();
        if (player == null || pokemonHealedEvent.getSource() != HealingSource.Force.INSTANCE) {
            return;
        }

        ItemStack teraOrb = AccessoriesUtils.findFirstItemWithTag(player, MegaShowdownTags.Items.TERA_ORB);
        if (teraOrb != ItemStack.EMPTY) {
            teraOrb.setDamageValue(0);
        }
    }

    private static void terrastallizationUsed(TerastallizationEvent event) {
        PokemonEntity pokemonEntity = event.getPokemon().getEffectedPokemon().getEntity();
        Pokemon pokemon = pokemonEntity.getPokemon();

        AspectPropertyType.INSTANCE.fromString("msd:tera_" + pokemon.getTeraType().showdownId()).apply(pokemon);
        AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "tera/terastallized");

        pokemon.getPersistentData().putBoolean("is_tera", true);
        if (MegaShowdownConfig.legacyTeraEffect) {
            GlowHandler.applyTeraGlow(pokemonEntity);
        }

        ServerPlayer player = pokemon.getOwnerPlayer();
        if (!PlayerUtils.hasPokemon(player, "Terapagos")) {
            ItemStack teraOrb = AccessoriesUtils.findFirstItemWithTag(player, MegaShowdownTags.Items.TERA_ORB);
            if (teraOrb != ItemStack.EMPTY) {
                teraOrb.setDamageValue(teraOrb.getDamageValue() + 10);
            }
        }

        event.getBattle().dispatchWaitingToFront(2f, () -> {
            PokemonBehaviourHelper.Companion.playAnimation(pokemonEntity, Set.of("cry"), List.of());
            return Unit.INSTANCE;
        });

        BlockPos entityPos = pokemonEntity.getOnPos();
        pokemonEntity.level().playSound(
                null, entityPos.getX(), entityPos.getY(), entityPos.getZ(),
                MegaShowdownSounds.TERASTALLIZATION.get(),
                SoundSource.PLAYERS, 0.2f, 0.8f
        );
    }

    private static void hookBattlePre(BattleStartedEvent.Pre event) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        event.getBattle().getActivePokemon().forEach(pkmn -> {
            if (pkmn.getBattlePokemon().getEffectedPokemon().getAspects().contains("core-percent")) {
                event.cancel();
                cancelled.set(true);
            }
        });
        if (cancelled.get()) {
            return;
        }

        event.getBattle().getPlayers().forEach(serverPlayer -> {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer);
            AspectUtils.revertPokemonsIfRequiredBattleStart(playerPartyStore);
        });
    }

    private static void hookBattleStarted(BattleStartedEvent.Post event) {
        event.getBattle().getOnEndHandlers().add((battle -> {
            battle.getPlayers().forEach(AspectUtils::revertPokemonsIfRequiredBattleEnd);
            return Unit.INSTANCE;
        }));
    }

    private static void megaEvolution(MegaEvolutionEvent event) {
        Pokemon pokemon = event.getPokemon().getEntity().getPokemon();
        MegaGimmick.megaEvolveInBattle(
                pokemon,
                event.getPokemon()
        );
    }

    private static void heldItemChange(HeldItemEvent.Pre event) {
        if (event.getPokemon().getPersistentData().getBoolean("form_changing")) {
            event.cancel();
            return;
        }
        Pokemon pokemon = event.getPokemon();

        ItemStack itemReceiving = event.getReceiving();
        ItemStack itemReturning = event.getReturning();

        if (itemReturning.getItem() == event.getReceiving().getItem()) {
            return;
        }

        if (itemReturning.getItem() instanceof FormChangeHeldItem formChangeItem) {
            formChangeItem.revert(pokemon);
        }

        if (itemReceiving.getItem() instanceof FormChangeHeldItem formChangeItem) {
            formChangeItem.apply(pokemon);
        }

        HeldItemFormChange heldItemFormChangeReturning = RegistryLocator.getComponent(HeldItemFormChange.class, itemReturning);

        if (heldItemFormChangeReturning != null) {
            heldItemFormChangeReturning.revert(pokemon);
        }

        HeldItemFormChange heldItemFormChangeReceiving = RegistryLocator.getComponent(HeldItemFormChange.class, itemReceiving);

        if (heldItemFormChangeReceiving != null) {
            heldItemFormChangeReceiving.apply(pokemon);
        }

        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, itemReturning);

        if (megaGimmick != null
                && megaGimmick.pokemons().contains(pokemon.getSpecies().getName())
                && MegaGimmick.isMega(pokemon)) {
            MegaGimmick.megaToggle(pokemon);
        }

        if (pokemon.getSpecies().getName().equals("Necrozma") && pokemon.getAspects().contains("ultra-fusion")) {
            ZCrystal zCrystal = RegistryLocator.getComponent(ZCrystal.class, itemReturning);

            if (zCrystal != null && zCrystal.showdown_item_id().equals("ultranecroziumz")) {
                UltraGimmick.ultraBurstToggle(pokemon);
            }
        }
    }
}
