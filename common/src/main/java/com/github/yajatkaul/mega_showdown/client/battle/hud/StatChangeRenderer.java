package com.github.yajatkaul.mega_showdown.client.battle.hud;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import com.github.yajatkaul.mega_showdown.client.battle.storage.BattlePokemonMemory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.Locale;
import java.util.Map;

public final class StatChangeRenderer {
    private StatChangeRenderer () {}

    private static final int TEXT_HEIGHT = 11;
    private static final Font TEXT_RENDERER = Minecraft.getInstance().font;

    public static void render (GuiGraphics context, BattlePokemonMemory memory, boolean isLeft, int order, boolean isCompact) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        int x;
        int y = isCompact ? BattleOverlay.VERTICAL_INSET + order * BattleOverlay.COMPACT_TILE_HEIGHT : BattleOverlay.VERTICAL_INSET + BattleOverlay.TILE_HEIGHT - 1;

        if (isLeft) {
            x = isCompact ? BattleOverlay.HORIZONTAL_INSET + BattleOverlay.TILE_WIDTH - 5 - order * 4 : BattleOverlay.HORIZONTAL_INSET;
        }
        else {
            x = isCompact ? screenWidth - BattleOverlay.HORIZONTAL_INSET - BattleOverlay.TILE_WIDTH + 4 + order * 4 : screenWidth - BattleOverlay.HORIZONTAL_INSET - 1;
        }

        int iterations = 0;

        for (Map.Entry<String, Integer> statChange : memory.getStatChanges().entrySet()) {
            if (statChange.getValue() == 0) continue;

            MutableComponent text = Component.literal(statChange.getKey().toUpperCase(Locale.ROOT));
            if (statChange.getValue() > 0) text.append(" +" + statChange.getValue());
            else text.append(" " + statChange.getValue());

            text.setStyle(Style.EMPTY.withFont(CobblemonResources.INSTANCE.getDEFAULT_LARGE()).withBold(true));

            if (isLeft) {
                x += renderBorderedText(context, x, y, text);
            }
            else {
                int textWidth = TEXT_RENDERER.width(text);
                x -= textWidth + 4;
                renderBorderedText(context, x, y, text);
            }

            if (++iterations % 4 == 0) {
                x = isLeft ? BattleOverlay.HORIZONTAL_INSET : screenWidth - BattleOverlay.HORIZONTAL_INSET - 1;
                y += TEXT_HEIGHT - 1;
            }
        }
    }

    private static int renderBorderedText (GuiGraphics context, int x, int y, Component text) {
        int textWidth = TEXT_RENDERER.width(text) + 5;
        context.fill(x, y, x + textWidth, y + TEXT_HEIGHT, 0xFF8D8D8D);
        context.fill(x + 2, y + 2, x + textWidth - 2, y + TEXT_HEIGHT - 2, 0xFF676767);
        context.renderOutline(x, y, textWidth, TEXT_HEIGHT, 0xFF2F2F2F);
        context.drawString(TEXT_RENDERER, text, x + 3, y + 1, -1, false);
        return textWidth - 1;
    }
}
