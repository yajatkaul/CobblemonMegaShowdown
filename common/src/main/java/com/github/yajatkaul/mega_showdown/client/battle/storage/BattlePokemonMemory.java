package com.github.yajatkaul.mega_showdown.client.battle.storage;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt;
import com.cobblemon.mod.common.client.gui.TypeIcon;
import com.cobblemon.mod.common.client.render.RenderHelperKt;
import com.cobblemon.mod.common.client.render.models.blockbench.FloatingState;
import com.cobblemon.mod.common.entity.PoseType;
import com.cobblemon.mod.common.item.CobblemonItem;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.util.math.QuaternionUtilsKt;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattlePokemonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BattlePokemonMemory {
    public static final int PANEL_WIDTH = 150;
    public static final int PANEL_HEIGHT = 104;
    private static final int FRAME_LENGTH = 28;
    private static final ResourceLocation PANEL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/info_panel.png");
    private static final ResourceLocation PANEL_FRAME_TEXTURE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/info_panel_frame.png");
    private static final Set<String> SKIP_MOVE_MEMORY = Set.of("metronome", "copycat");

    private final UUID uuid;
    private final List<Illusory<Move>> knownMoves = new ArrayList<>();
    private double healthPercentage = 1;
    private double prevHealthPercentage = 1;
    private String status = null;
    private Illusory<String> item = null;
    private String ability = null;
    private String tempAbility = null;
    private RenderablePokemon renderablePokemon;
    private String name = null;
    private String owner = null;
    private String lastMove;
    private boolean active;
    private boolean consumedItem = false;
    private boolean transformed = false;
    private boolean illusionBroken = false;
    private final Map<String, Integer> statChanges = new HashMap<>();

    public BattlePokemonMemory(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid () {
        return this.uuid;
    }

    public void setState (BattlePokemonState state) {
        if (state.moves().isPresent()) {
            state.moves().get()
                .stream()
                .map(Moves::getByName)
                .filter(Objects::nonNull)
                .map(template -> template.create(template.getMaxPp(), 3))
                .forEach(this::addMove);
        }
        this.healthPercentage = state.healthPercentage();
        this.status = state.status().orElse(null);
        if (state.item().isPresent()) this.item = new Illusory<>(true, state.item().get());
        if (state.pokemonProperties().isPresent()) {
            PokemonProperties props = state.pokemonProperties().get().get();
            if (props.hasSpecies()) this.renderablePokemon = props.asRenderablePokemon();
            if (props.getAbility() != null) this.ability = props.getAbility();
        }
        this.statChanges.clear();
        this.statChanges.putAll(state.statChanges());
    }

    public void setRenderablePokemon (RenderablePokemon renderablePokemon) {
        this.renderablePokemon = renderablePokemon;
    }

    public boolean isActive () {
        return this.active;
    }

    public void setActive (boolean active) {
        this.active = active;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getOwner () {
        return this.owner;
    }

    public void setOwner (String owner) {
        this.owner = owner;
    }

    public void setMoves (MoveSet moves) {
        this.knownMoves.clear();
        moves.forEach(move -> this.knownMoves.add(new Illusory<>(true, move)));
        while (this.knownMoves.size() < 4) {
            this.knownMoves.add(new Illusory<>(true, MoveTemplate.Companion.dummy("empty").create()));
        }
    }

    public void addMove (Move move) {
        if (this.knownMoves.stream().map(Illusory::getData).anyMatch(template -> template.getName().equalsIgnoreCase(move.getName())))
            return;

        if (this.knownMoves.size() < 4) {
            this.knownMoves.add(new Illusory<>(this.illusionBroken, move));
        }
        else {
            Optional<Illusory<Move>> toRemove = this.knownMoves.stream().filter(Illusory::canBeCleared).findFirst();
            if (toRemove.isEmpty()) {
                toRemove = this.knownMoves.stream().filter(iMove -> !iMove.isConfirmed()).findFirst();
            }

            if (toRemove.isPresent()) {
                this.knownMoves.remove(toRemove.get());
                this.knownMoves.add(new Illusory<>(this.illusionBroken, move));
            }
        }
    }

    public void useMove (String move) {
        if (this.lastMove != null && SKIP_MOVE_MEMORY.contains(this.lastMove)) {
            this.lastMove = move;
            return;
        }
        else if (this.transformed) {
            return;
        }
        this.lastMove = move;

        Optional<Illusory<Move>> existing = this.knownMoves.stream()
            .filter(move1 -> move1.getData().getName().equalsIgnoreCase(move))
            .findAny();

        if (existing.isPresent()) {
            existing.get().getData().setCurrentPp(existing.get().getData().getCurrentPp() - 1);
            if (this.illusionBroken) existing.get().confirmed = true;
        }
        else {
            MoveTemplate template = Moves.getByNameOrDummy(move);
            Move realMove = template.create(template.getMaxPp(), 3);
            realMove.setCurrentPp(realMove.getCurrentPp() - 1);
            this.addMove(realMove);
        }
    }

    public Move getMove (int index) {
        if (index >= this.knownMoves.size()) return MoveTemplate.Companion.dummy("unknown").create();
        return this.knownMoves.get(index).getData();
    }

    public Map<String, Integer> getStatChanges () {
        return this.statChanges;
    }

    public double lerpHealthPercentage (double tickDelta) {
        return Mth.lerp(tickDelta, this.prevHealthPercentage, this.healthPercentage);
    }

    public double getHealthPercentage () {
        return this.healthPercentage;
    }

    public void setHealthPercentage (double healthPercentage) {
        this.prevHealthPercentage = this.healthPercentage;
        this.healthPercentage = Math.clamp(healthPercentage, 0, 1);
    }

    public boolean isAlive () {
        return this.healthPercentage > 0;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public boolean hasStatus () {
        return this.status != null;
    }

    public String getItem () {
        return this.item == null ? null : this.item.getData();
    }

    public void setItem (String item) {
        this.item = new Illusory<>(this.illusionBroken, item);
    }

    public void setItem (CobblemonItem item) {
        this.item = new Illusory<>(this.illusionBroken, item.getDescriptionId());
    }

    public void setConsumedItem (boolean consumedItem) {
        this.consumedItem = consumedItem;
    }

    public String getAbility () {
        return this.ability;
    }

    public void setAbility (String ability) {
        this.ability = ability;
    }

    public void setTempAbility (String tempAbility) {
        this.tempAbility = tempAbility;
    }

    public boolean isTransformed () {
        return this.transformed;
    }

    public void setTransformed (boolean transformed) {
        this.transformed = transformed;
    }

    public void onSendOut () {
        this.illusionBroken = false;
        this.statChanges.clear();
    }

    public void onRemovedFromField () {
        if (this.item != null) this.item.markOld();
        this.knownMoves.forEach(Illusory::markOld);
        this.tempAbility = null;
        this.transformed = false;
        this.statChanges.clear();
    }

    public void clearIllusoryData () {
        this.knownMoves.removeIf(Illusory::canBeCleared);
        if (this.item != null && this.item.canBeCleared()) this.item = null;
    }

    public void confirmNoIllusion () {
        this.illusionBroken = true;
        for (Illusory<Move> move : this.knownMoves) {
            if (move.isNew()) move.confirmed = true;
        }
        if (this.item != null) this.item.confirmed = true;
    }

    public void transferIllusoryDataTo (BattlePokemonMemory other) {
        for (Illusory<Move> move : this.knownMoves) {
            if (move.canBeCleared()) other.useMove(move.getData().getName());
        }
        if (this.item != null && this.item.canBeCleared()) other.setItem(this.item.getData());

        other.statChanges.clear();
        other.statChanges.putAll(this.statChanges);

        other.confirmNoIllusion();
    }

    public void render (GuiGraphics context, int x, int y, float tickDelta, boolean isLeft) {
        // All Text
        {
            context.pose().pushPose();
            context.pose().translate(0, 0, 100);

            Component species;
            if (this.renderablePokemon != null) species = Component.translatable("cobblemon.species." + this.renderablePokemon.getSpecies().resourceIdentifier.getPath() + ".name");
            else species = Component.translatable("gui.battle.mega_showdown.field.unknown");

            context.drawString(
                Minecraft.getInstance().font,
                species,
                x + 30,
                y + 4,
                CommonColors.WHITE,
                true
            );
            this.renderForm(context, x, y);
            this.renderItem(context, x, y);
            this.renderAbility(context, x, y);
            this.renderMoves(context, x, y);
            context.pose().popPose();
        }

        context.blit(PANEL_TEXTURE, x, y, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, PANEL_WIDTH, PANEL_HEIGHT);
        // Frame
        {
            context.pose().pushPose();
            context.enableScissor(x + 4, y + 4, x + 23, y + 23);
            context.pose().translate(x + 15, y - 5, 0);
            if (this.renderablePokemon != null) {
                PokemonGuiUtilsKt.drawProfilePokemon(
                    this.renderablePokemon,
                    context.pose(),
                    QuaternionUtilsKt.fromEulerXYZDegrees(new Quaternionf(), new Vector3f(13f, isLeft ? -35f : 35f, 0f)),
                    PoseType.PROFILE,
                    new FloatingState(),
                    tickDelta,
                    16f,
                    true,
                    false,
                    1f, 1f, 1f, 1f,
                    0f, 0f
                );
            }
            context.disableScissor();
            context.pose().popPose();
            context.blit(PANEL_FRAME_TEXTURE, x, y, 1000, 0, 0, FRAME_LENGTH, FRAME_LENGTH, FRAME_LENGTH, FRAME_LENGTH);
        }

        // Types
        if (this.renderablePokemon != null) {
            context.pose().pushPose();
            final float scale = 1.4f;
            context.pose().scale(scale, scale, 1);
            new TypeIcon(
                (x + 16) / scale, (y + 30) / scale,
                this.renderablePokemon.getForm().getPrimaryType(),
                this.renderablePokemon.getForm().getSecondaryType(),
                true,
                true,
                7f,
                3.5f,
                1f
            ).render(context);
            context.pose().popPose();
        }
    }

    private MutableComponent getMoveName (Move template) {
        if (template.getName().equalsIgnoreCase("unknown")) return Component.translatable("gui.battle.mega_showdown.field.unknown");
        if (template.getName().equalsIgnoreCase("empty")) return Component.translatable("gui.battle.mega_showdown.field.empty");
        return Component.translatableWithFallback("cobblemon.move." + template.getName(), template.getName());
    }

    private MutableComponent getMovePP (Move move) {
        if (move.getName().equalsIgnoreCase("unknown") || move.getName().equalsIgnoreCase("empty")) {
            return Component.translatable("gui.battle.mega_showdown.move.pp", "-", "-");
        }
        return Component.translatable("gui.battle.mega_showdown.move.pp", move.getCurrentPp(), move.getMaxPp());
    }

    private void renderForm (GuiGraphics context, int x, int y) {
        RenderHelperKt.drawScaledText(
            context,
            null,
            Component.translatable("gui.battle.mega_showdown.form"),
            x + 86.5, y + 16.5,
            0.5f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.LIGHTER_GRAY,
            true,
            false,
            null, null
        );

        MutableComponent formName;
        if (this.renderablePokemon == null) {
            formName = Component.translatable("gui.battle.mega_showdown.field.unknown");
        }
        else if (this.renderablePokemon.getForm().formOnlyShowdownId().equalsIgnoreCase("normal")){
            formName = Component.translatable("gui.battle.mega_showdown.field.empty");
        }
        else {
            formName = Component.translatableWithFallback("cobblemon.ui.pokedex.info.form." + this.renderablePokemon.getSpecies().showdownId() + "-" + this.renderablePokemon.getForm().formOnlyShowdownId(), this.renderablePokemon.getForm().formOnlyShowdownId());
        }

        RenderHelperKt.drawScaledText(
            context,
            null,
            formName,
            x + 86.5, y + 23,
            0.75f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.WHITE,
            true,
            true,
            null, null
        );
    }

    private void renderItem (GuiGraphics context, int x, int y) {
        RenderHelperKt.drawScaledText(
            context,
            null,
            Component.translatable("gui.battle.mega_showdown.item"),
            x + 86.5, y + 30.5,
            0.5f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.LIGHTER_GRAY,
            true,
            false,
            null, null
        );

        MutableComponent item;
        if (this.consumedItem) item = Component.translatable("gui.battle.mega_showdown.field.empty");
        else if (this.item != null) {
            if (this.item.getData().isEmpty()) {
                item = Component.translatable("gui.battle.mega_showdown.field.empty");
            }
            else {
                Language lang = Language.getInstance();
                if (lang.has(this.item.getData())) item = Component.translatable(this.item.getData());
                else if (lang.has("item.cobblemon." + this.item)) item = Component.translatable("item.cobblemon." + this.item);
                else item = Component.literal(this.item.getData());
            }
        }
        else item = Component.translatable("gui.battle.mega_showdown.field.unknown");

        RenderHelperKt.drawScaledText(
            context,
            null,
            item,
            x + 86.5, y + 36.5,
            0.75f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.WHITE,
            true,
            true,
            null, null
        );
    }

    private void renderAbility (GuiGraphics context, int x, int y) {
        RenderHelperKt.drawScaledText(
            context,
            null,
            Component.translatable("gui.battle.mega_showdown.ability"),
            x + 75, y + 44.5,
            0.5f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.LIGHTER_GRAY,
            true,
            false,
            null, null
        );

        MutableComponent ability = null;
        if (this.ability != null) {
            if (this.tempAbility != null) {
                ability = Component.translatable(
                    "gui.battle.mega_showdown.ability.temp",
                    Component.translatableWithFallback("cobblemon.ability." + this.ability, this.ability),
                    Component.translatableWithFallback("cobblemon.ability." + this.tempAbility, this.tempAbility)
                );
            }
            else {
                ability = Component.translatableWithFallback("cobblemon.ability." + this.ability, this.ability);
            }
        }
        else if (this.tempAbility != null) {
            ability = Component.translatable(
                "gui.battle.mega_showdown.ability.temp",
                Component.translatable("gui.battle.mega_showdown.field.unknown"),
                Component.translatableWithFallback("cobblemon.ability." + this.tempAbility, this.tempAbility)
            );
        }
        else if (this.renderablePokemon == null) {
            ability = Component.translatable("gui.battle.mega_showdown.field.unknown");
        }

        if (ability == null) {
            ability = Component.empty();
            Iterator<PotentialAbility> iterator = this.renderablePokemon.getForm().getAbilities().iterator();
            while (iterator.hasNext()) {
                PotentialAbility potentialAbility = iterator.next();
                ability.append(Component.translatableWithFallback(potentialAbility.getTemplate().getDisplayName(), potentialAbility.getTemplate().getName()));

                if (iterator.hasNext()) {
                    ability.append(" / ");
                }
            }
        }

        float scale = Minecraft.getInstance().font.width(ability) > 139 ? 0.5f : 0.75f;

        RenderHelperKt.drawScaledText(
            context,
            null,
            ability,
            x + 75, y + 51,
            scale,
            1f,
            Integer.MAX_VALUE,
            CommonColors.WHITE,
            true,
            true,
            null, null
        );
    }

    private void renderMoves (GuiGraphics context, int x, int y) {
        RenderHelperKt.drawScaledText(
            context,
            null,
            Component.translatable("gui.battle.mega_showdown.moves"),
            x + 75, y + 64.5,
            0.5f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.LIGHTER_GRAY,
            true,
            false,
            null, null
        );

        this.renderMove(context, this.getMove(0), x + 39.5, y + 70.5);
        this.renderMove(context, this.getMove(1), x + 110.5, y + 70.5);
        this.renderMove(context, this.getMove(2), x + 39.5, y + 86.5);
        this.renderMove(context, this.getMove(3), x + 110.5, y + 86.5);
    }

    private void renderMove (GuiGraphics context, Move move, double x, double y) {
        Font textRenderer = Minecraft.getInstance().font;

        MutableComponent moveName = this.getMoveName(move);
        float scale1 = textRenderer.width(moveName) > 68 ? 0.5f : 0.75f;
        RenderHelperKt.drawScaledText(
            context,
            null,
            moveName,
            x, y,
            scale1,
            1f,
            Integer.MAX_VALUE,
            CommonColors.WHITE,
            true,
            true,
            null, null
        );

        MutableComponent ppCount = this.getMovePP(move);
        RenderHelperKt.drawScaledText(
            context,
            null,
            ppCount,
            x, y + 7,
            0.5f,
            1f,
            Integer.MAX_VALUE,
            CommonColors.WHITE,
            true,
            true,
            null, null
        );
    }

    private static class Illusory<T> {
        private boolean confirmed;
        private boolean old;
        private final T data;

        private Illusory (boolean confirmed, T data) {
            this.confirmed = confirmed;
            this.data = data;
            this.old = false;
        }

        private boolean isConfirmed () {
            return this.confirmed;
        }

        private boolean isNew () {
            return !this.old;
        }

        private void markOld () {
            this.old = true;
        }

        private boolean canBeCleared () {
            return !this.old && !this.confirmed;
        }

        private T getData () {
            return this.data;
        }
    }
}
