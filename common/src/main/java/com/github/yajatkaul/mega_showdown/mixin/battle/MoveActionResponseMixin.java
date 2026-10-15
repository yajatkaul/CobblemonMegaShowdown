package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.*;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.github.yajatkaul.mega_showdown.gimmick.MaxBond;
import kotlin.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = MoveActionResponse.class, remap = false)
public class MoveActionResponseMixin {
    @Shadow
    private String moveName;
    @Shadow
    private String targetPnx;
    @Shadow
    private String gimmickID;

    /**
     * @author YajatKaul, Provismet
     * @reason TargetSelection
     */
    @Overwrite
    public boolean isValid(ActiveBattlePokemon activeBattlePokemon, ShowdownMoveset showdownMoveSet, boolean forceSwitch) {
        if (forceSwitch || showdownMoveSet == null) {
            return false;
        }

        ShowdownMoveset.Gimmick selectedGimmick = null;
        if (gimmickID != null) {
            for (ShowdownMoveset.Gimmick gimmick : ShowdownMoveset.Gimmick.values()) {
                if (gimmick.getId().equals(gimmickID)) {
                    selectedGimmick = gimmick;
                    break;
                }
            }
            if (selectedGimmick == null) {
                return false;
            }
        }

        boolean isMegaEvolution = selectedGimmick == ShowdownMoveset.Gimmick.MEGA_EVOLUTION;
        boolean isZMove = selectedGimmick == ShowdownMoveset.Gimmick.Z_POWER;

        if (isMegaEvolution || isZMove) {
            BattlePokemon battlePokemon = activeBattlePokemon.getBattlePokemon();
            if (battlePokemon == null || !MaxBond.hasRequiredFriendship(battlePokemon.getOriginalPokemon())) {
                return false;
            }

            if (isMegaEvolution && !showdownMoveSet.getCanMegaEvo()) {
                return false;
            }
            if (isZMove && showdownMoveSet.getCanZMove() == null) {
                return false;
            }
        }

        InBattleMove move = showdownMoveSet.getMoves().stream()
                .filter(m -> m.getId().equals(moveName))
                .findFirst()
                .orElse(null);
        if (move == null) return false;

        InBattleGimmickMove gimmickMove = move.getGimmickMove();
        boolean validGimmickMove = gimmickMove != null && !gimmickMove.getDisabled();
        if (isZMove && !validGimmickMove) {
            return false;
        }
        if (!validGimmickMove && !move.canBeUsed()) {
            return false;
        }

        List<Targetable> availableTargets;
        if (selectedGimmick != null
                && validGimmickMove
                && selectedGimmick != ShowdownMoveset.Gimmick.MEGA_EVOLUTION
                && selectedGimmick != ShowdownMoveset.Gimmick.TERASTALLIZATION) {
            availableTargets = gimmickMove.getTarget().getTargetList().invoke(activeBattlePokemon);
        } else {
            availableTargets = move.getTarget().getTargetList().invoke(activeBattlePokemon);
        }

        if (availableTargets == null || availableTargets.isEmpty()) return true;
        if (this.targetPnx == null) return false;
        Pair<BattleActor, ActiveBattlePokemon> targetPair = activeBattlePokemon.getActor().getBattle().getActorAndActiveSlotFromPNX(this.targetPnx);
        return availableTargets.contains(targetPair.getSecond());
    }
}
