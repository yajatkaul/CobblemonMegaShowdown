package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.battles.InBattleGimmickMove;
import com.cobblemon.mod.common.battles.InBattleMove;
import com.cobblemon.mod.common.battles.MoveTarget;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleTargetSelection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BattleTargetSelection.class, remap = false)
public abstract class BattleTargetSelectionMixin {
    @Final
    @Shadow
    private String gimmickID;
    @Final
    @Shadow private InBattleMove move;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/battles/InBattleGimmickMove;getTarget()Lcom/cobblemon/mod/common/battles/MoveTarget;")
    )
    private MoveTarget redirectTargetType(InBattleGimmickMove instance) {
        if (gimmickID != null && instance != null
                && !gimmickID.equals("terastal") && !gimmickID.equals("mega")) {
            return instance.getTarget();
        }

        return move.getTarget();
    }
}
