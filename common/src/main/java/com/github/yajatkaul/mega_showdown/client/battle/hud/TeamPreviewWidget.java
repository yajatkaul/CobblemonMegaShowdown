package com.github.yajatkaul.mega_showdown.client.battle.hud;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamPreviewWidget extends AbstractWidget {
    public static final int BORDER_HEIGHT = 10;
    public static final int WIDTH = PokeballPreviewWidget.WIDTH;
    private static final ResourceLocation TOP_LEFT_BORDER = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/top_left.png");
    private static final ResourceLocation BOTTOM_LEFT_BORDER = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/bottom_left.png");
    private static final ResourceLocation TOP_RIGHT_BORDER = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/top_right.png");
    private static final ResourceLocation BOTTOM_RIGHT_BORDER = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "textures/gui/battle/bottom_right.png");

    private final List<PokeballPreviewWidget> party = new ArrayList<>();
    private final boolean isLeft;

    public TeamPreviewWidget (int x, int y, boolean isLeft) {
        super(x, y, WIDTH, BORDER_HEIGHT * 2, Component.literal(""));
        this.isLeft = isLeft;
    }

    @Override
    protected void renderWidget (GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.party.isEmpty()) return;

        context.blit(isLeft ? TOP_LEFT_BORDER : TOP_RIGHT_BORDER, this.getX(), this.getY(), 0, 0, WIDTH, BORDER_HEIGHT, WIDTH, BORDER_HEIGHT);
        this.party.forEach(widget -> widget.render(context, mouseX, mouseY, delta));
        context.blit(isLeft ? BOTTOM_LEFT_BORDER : BOTTOM_RIGHT_BORDER, this.getX(), this.getY() + this.getHeight() - BORDER_HEIGHT, 0, 0, WIDTH, BORDER_HEIGHT, WIDTH, BORDER_HEIGHT);
    }

    public void realignToScreen () {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.setX(this.isLeft ? 0 : screenWidth - WIDTH);
        this.setY((screenHeight - this.getHeight()) / 2);

        for (int i = 0; i < this.party.size(); ++i) {
            PokeballPreviewWidget widget = this.party.get(i);
            widget.setX(this.getX());
            widget.setY(this.getY() + BORDER_HEIGHT + i * widget.getHeight());
        }
    }

    public void addPartyMember (PokeballPreviewWidget widget) {
        this.party.add(widget);
        this.height += PokeballPreviewWidget.HEIGHT;
    }

    public boolean hasPartyMember (UUID uuid) {
        return this.party.stream().anyMatch(widget -> widget.getBattleMemory().getUuid().equals(uuid));
    }

    public void clearParty () {
        this.party.clear();
        this.height = BORDER_HEIGHT * 2;
    }

    public int getPartySize () {
        return this.party.size();
    }

    public boolean isLeft () {
        return this.isLeft;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
