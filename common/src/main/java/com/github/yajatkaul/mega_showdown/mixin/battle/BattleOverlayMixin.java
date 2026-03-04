package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import com.github.yajatkaul.mega_showdown.client.battle.hud.BattleHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BattleOverlay.class)
public abstract class BattleOverlayMixin extends Gui {
    private BattleOverlayMixin (Minecraft client) {
        super(client);
    }

    @Inject(method = "drawTile", at = @At("TAIL"))
    private void drawStatChanges (
        GuiGraphics context,
        float tickDelta,
        ActiveClientBattlePokemon activeBattlePokemon,
        boolean left,
        int rank,
        PokedexEntryProgress dexState,
        boolean hasCommand,
        boolean isHovered,
        boolean isCompact,
        CallbackInfo info
    ) {
        BattleHud.drawStatChanges(context, activeBattlePokemon, left, rank, isCompact);
    }
}
