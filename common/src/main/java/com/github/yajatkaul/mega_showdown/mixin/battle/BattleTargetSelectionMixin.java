package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.battles.InBattleGimmickMove;
import com.cobblemon.mod.common.battles.InBattleMove;
import com.cobblemon.mod.common.battles.MoveTarget;
import com.cobblemon.mod.common.client.battle.SingleActionRequest;
import com.cobblemon.mod.common.client.gui.battle.BattleGUI;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleTargetSelection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BattleTargetSelection.class, remap = false)
public abstract class BattleTargetSelectionMixin {
    @Final
    @Shadow
    private String gimmickID;
    @Final
    @Shadow
    private InBattleMove move;
    @Unique
    private InBattleGimmickMove mega_showdown$capturedGimmickMove;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureGimmickMove(
            BattleGUI battleGUI,
            SingleActionRequest request,
            InBattleMove move,
            String gimmickID,
            InBattleGimmickMove gimmickMove,
            CallbackInfo ci
    ) {
        this.mega_showdown$capturedGimmickMove = gimmickMove;
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/battles/InBattleGimmickMove;getTarget()Lcom/cobblemon/mod/common/battles/MoveTarget;")
    )
    private MoveTarget redirectTargetType(InBattleGimmickMove instance) {
        if (gimmickID != null && mega_showdown$capturedGimmickMove != null
                && !gimmickID.equals("terastal") && !gimmickID.equals("mega")) {
            return mega_showdown$capturedGimmickMove.getTarget();
        }

        return move.getTarget();
    }
}
