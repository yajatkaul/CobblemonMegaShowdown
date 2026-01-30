package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.battles.InBattleMove;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleGimmickButton;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleMoveSelection;
import com.cobblemon.mod.common.util.math.SimpleMathExtensionsKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BattleGimmickButton.GimmickTile.class, remap = false)
public class GimmickTileMixin {
    @Unique
    protected BattleMoveSelection moveSelection;
    @Unique
    protected InBattleMove move;
    @Final
    @Shadow
    private ShowdownMoveset.Gimmick gimmick;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(ShowdownMoveset.Gimmick gimmick, BattleMoveSelection moveSelection, InBattleMove move, float x, float y, CallbackInfo ci) {
        BattleGimmickButton.GimmickTile tile = (BattleGimmickButton.GimmickTile) (Object) this;
        this.moveSelection = tile.getMoveSelection();
        this.move = tile.getMove();

        if (gimmick == ShowdownMoveset.Gimmick.TERASTALLIZATION || gimmick == ShowdownMoveset.Gimmick.MEGA_EVOLUTION) {
            tile.setMoveTemplate(Moves.getByNameOrDummy(move.id));
            tile.setRgb(SimpleMathExtensionsKt.toRGB(tile.getMoveTemplate().getEffectiveElementalType(tile.getPokemon()).getHue()));
        }
    }
}