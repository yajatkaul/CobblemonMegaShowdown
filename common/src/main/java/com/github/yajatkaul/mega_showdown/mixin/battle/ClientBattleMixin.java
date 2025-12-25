package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.battles.MoveActionResponse;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.client.battle.ClientBattle;
import com.cobblemon.mod.common.client.battle.SingleActionRequest;
import com.github.yajatkaul.mega_showdown.utils.duck.cobblemon.interfaces.ClientBattleDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = ClientBattle.class, remap = false)
public class ClientBattleMixin implements ClientBattleDuck {
    @Shadow
    private List<SingleActionRequest> pendingActionRequests;

    @Override
    public boolean pendingGimmick(ShowdownMoveset.Gimmick gimmick) {
        for (SingleActionRequest request : this.pendingActionRequests) {
            if (request.getResponse() instanceof MoveActionResponse response) {
                if (gimmick.getId().equals(response.getGimmickID())) {
                    return false;
                }
            }
        }
        return true;
    }
}