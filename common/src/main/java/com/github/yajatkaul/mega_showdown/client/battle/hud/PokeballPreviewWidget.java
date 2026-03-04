package com.github.yajatkaul.mega_showdown.client.battle.hud;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ClientBattle;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.client.battle.storage.BattlePokemonMemory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PokeballPreviewWidget extends AbstractWidget {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static final int HP_WIDTH = 1;
    public static final int HP_HEIGHT = 13;

    private static final ResourceLocation ALIVE_LEFT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_left.png");
    private static final ResourceLocation ALIVE_LEFT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_left_hovered.png");
    private static final ResourceLocation ALIVE_LEFT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_left_active.png");
    private static final ResourceLocation ALIVE_RIGHT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_right.png");
    private static final ResourceLocation ALIVE_RIGHT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_right_hovered.png");
    private static final ResourceLocation ALIVE_RIGHT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_right_active.png");
    private static final ResourceLocation ALIVE_HP = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/alive_health_bar.png");

    private static final ResourceLocation STATUS_LEFT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_left.png");
    private static final ResourceLocation STATUS_LEFT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_left_hovered.png");
    private static final ResourceLocation STATUS_LEFT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_left_active.png");
    private static final ResourceLocation STATUS_RIGHT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_right.png");
    private static final ResourceLocation STATUS_RIGHT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_right_hovered.png");
    private static final ResourceLocation STATUS_RIGHT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_right_active.png");
    private static final ResourceLocation STATUS_HP = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/status_health_bar.png");

    private static final ResourceLocation FAINTED_LEFT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_left.png");
    private static final ResourceLocation FAINTED_LEFT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_left_hovered.png");
    private static final ResourceLocation FAINTED_LEFT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_left_active.png");
    private static final ResourceLocation FAINTED_RIGHT = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_right.png");
    private static final ResourceLocation FAINTED_RIGHT_HOVERED = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_right_hovered.png");
    private static final ResourceLocation FAINTED_RIGHT_ACTIVE = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/fainted_right_active.png");

    private final BattlePokemonMemory underlying;
    private final boolean isLeft;

    public PokeballPreviewWidget (boolean isLeft, BattlePokemonMemory state) {
        super(0, 0, WIDTH, HEIGHT, Component.literal("Pokemon"));
        this.isLeft = isLeft;
        this.underlying = state;
    }

    @Override
    protected void renderWidget (GuiGraphics context, int mouseX, int mouseY, float delta) {
        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (this.underlying == null || battle == null) return;

        ResourceLocation texture;
        if (this.isHovered() && !battle.getMinimised()) {
            if (!this.underlying.isAlive()) texture = this.isLeft ? FAINTED_LEFT_HOVERED : FAINTED_RIGHT_HOVERED;
            else if (this.underlying.hasStatus()) texture = this.isLeft ? STATUS_LEFT_HOVERED : STATUS_RIGHT_HOVERED;
            else texture = this.isLeft ? ALIVE_LEFT_HOVERED : ALIVE_RIGHT_HOVERED;

            this.underlying.render(
                context,
                this.isLeft ? this.getX() + WIDTH + 5 : this.getX() - BattlePokemonMemory.PANEL_WIDTH - 5,
                (Minecraft.getInstance().getWindow().getGuiScaledHeight() - BattlePokemonMemory.PANEL_HEIGHT) / 2,
                delta,
                this.isLeft
            );
        }
        else if (this.underlying.isActive()) {
            if (!this.underlying.isAlive()) texture = this.isLeft ? FAINTED_LEFT_ACTIVE : FAINTED_RIGHT_ACTIVE;
            else if (this.underlying.hasStatus()) texture = this.isLeft ? STATUS_LEFT_ACTIVE : STATUS_RIGHT_ACTIVE;
            else texture = this.isLeft ? ALIVE_LEFT_ACTIVE : ALIVE_RIGHT_ACTIVE;
        }
        else {
            if (!this.underlying.isAlive()) texture = this.isLeft ? FAINTED_LEFT : FAINTED_RIGHT;
            else if (this.underlying.hasStatus()) texture = this.isLeft ? STATUS_LEFT : STATUS_RIGHT;
            else texture = this.isLeft ? ALIVE_LEFT : ALIVE_RIGHT;
        }
        context.blit(texture, this.getX(), this.getY(), 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        if (this.underlying.isAlive()) {
            ResourceLocation hpTexture = this.underlying.hasStatus() ? STATUS_HP : ALIVE_HP;

            int hpX = this.isLeft ? this.getX() + 1 : this.getX() + WIDTH - 2;
            int hpHeight = (int)(this.underlying.lerpHealthPercentage(delta) * HP_HEIGHT);
            context.blit(
                hpTexture,
                hpX, this.getY() + 4 + (HP_HEIGHT - hpHeight), 1,
                0, 1f - (float)this.underlying.getHealthPercentage(),
                HP_WIDTH, hpHeight,
                HP_WIDTH, hpHeight
            );
        }
    }

    @Override
    protected boolean isValidClickButton (int button) {
        return false;
    }

    @Override
    protected void updateWidgetNarration (NarrationElementOutput narrationElementOutput) {

    }

    public BattlePokemonMemory getBattleMemory () {
        return this.underlying;
    }
}
