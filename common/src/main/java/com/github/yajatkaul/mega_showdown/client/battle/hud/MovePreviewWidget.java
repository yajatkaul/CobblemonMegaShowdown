package com.github.yajatkaul.mega_showdown.client.battle.hud;

import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

public class MovePreviewWidget extends AbstractWidget {
    public static final int WIDTH = 193;
    public static final int HEIGHT = 68;
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/move_preview.png");
    private static final Font TEXT_RENDERER = Minecraft.getInstance().font;
    private final MoveTemplate move;
    private boolean isSTAB = false;

    public MovePreviewWidget(MoveTemplate move) {
        super(0, 0, WIDTH, HEIGHT, Component.literal("Move"));
        this.move = move;
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.realignToScreen();
        context.pose().pushPose();
        context.pose().translate(0, 0, 100);

        context.blit(TEXTURE, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        Component none = Component.literal("-");
        Component power = this.move.getPower() > 0 ? this.stabPower() : none;
        Component effectChance = this.move.getEffectChances().length == 0 ? Component.literal("-") : Component.literal(String.valueOf(this.move.getEffectChances()[0].intValue())).append("%");
        Component accuracy = this.move.getAccuracy() > 0 ? Component.literal(String.valueOf((int) this.move.getAccuracy())).append("%") : none;

        int powerWidth = TEXT_RENDERER.width(power);
        int effectWidth = TEXT_RENDERER.width(effectChance);
        int accuracyWidth = TEXT_RENDERER.width(accuracy);

        float scale = 0.75f;
        context.pose().pushPose();
        context.pose().scale(scale, scale, 1);

        int leftTextStart = (int) ((this.getX() + 15) / scale);
        int leftNumberStart = leftTextStart + 104;
        int rightTextStart = (int) ((this.getX() + 107.5) / scale);
        int rightNumberStart = rightTextStart + 106;

        int row1Y = (int) ((this.getY() + 6.5) / scale);
        int row2Y = (int) ((this.getY() + 18.5) / scale);

        context.drawString(TEXT_RENDERER, Component.translatable("cobblemon.ui.power"), leftTextStart, row1Y, CommonColors.WHITE, false);
        context.drawString(TEXT_RENDERER, power, rightNumberStart - powerWidth, row1Y, CommonColors.WHITE, false);

        context.drawString(TEXT_RENDERER, Component.translatable("cobblemon.ui.effect"), leftTextStart, row2Y, CommonColors.WHITE, false);
        context.drawString(TEXT_RENDERER, effectChance, leftNumberStart - effectWidth, row2Y, CommonColors.WHITE, false);

        context.drawString(TEXT_RENDERER, Component.translatable("cobblemon.ui.accuracy"), rightTextStart, row2Y, CommonColors.WHITE, false);
        context.drawString(TEXT_RENDERER, accuracy, rightNumberStart - accuracyWidth, row2Y, CommonColors.WHITE, false);

        context.drawWordWrap(TEXT_RENDERER, this.move.getDescription(), (int) ((this.getX() + 6) / scale), (int) ((this.getY() + 35) / scale), (int) (182 / scale), CommonColors.WHITE);
        context.pose().popPose();

        context.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    private void realignToScreen() {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        this.setX(TeamPreviewWidget.WIDTH + 2);
        this.setY(screenHeight - HEIGHT - 90);
    }

    public void setSTAB(Pokemon pokemon, ElementalType type) {
        if (pokemon.getPrimaryType().equals(type)) this.isSTAB = true;
        else this.isSTAB = pokemon.getSecondaryType() != null && pokemon.getSecondaryType().equals(type);
    }

    private Component stabPower() {
        if (this.move.getPower() <= 1) { // Z-Moves have a power of 1
            return Component.literal("???");
        }

        int power = (int) this.move.getPower();
        MutableComponent text = Component.literal(String.valueOf(power));
        if (this.isSTAB) {
            int stabValue = (int) (this.move.getPower() * 1.5);
            Component stabText = Component.literal(String.valueOf(stabValue)).withStyle(ChatFormatting.YELLOW);
            text = Component.translatable("gui.battle.mega_showdown.move.power_with_stab", power, stabText);
        }

        return text;
    }
}
