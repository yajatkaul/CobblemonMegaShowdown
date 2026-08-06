package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.player.GeneralPlayerData;
import com.cobblemon.mod.common.battles.ActiveBattlePokemon;
import com.cobblemon.mod.common.battles.ShowdownActionRequest;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.github.yajatkaul.mega_showdown.gimmick.GimmickTurnCheck;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ShowdownActionRequest.class, remap = false)
public class ShowdownActionRequestMixin {
    @Shadow
    private List<ShowdownMoveset> active;

    @Inject(method = "sanitize", at = @At("HEAD"), remap = false)
    private void beforeSanitize(PokemonBattle battle, BattleActor battleActor, CallbackInfo ci) {
        battle.getPlayers().forEach(GimmickTurnCheck::check);
    }

    @Inject(method = "sanitize", at = @At("TAIL"), remap = false)
    private void afterSanitize(PokemonBattle battle, BattleActor battleActor, CallbackInfo ci) {
        ResourceLocation dynamaxBandId = ResourceLocation.fromNamespaceAndPath("cobblemon", "dynamax_band");
        List<ShowdownMoveset> activeMovesets = active;

        for (ServerPlayer player : battle.getPlayers()) {
            GeneralPlayerData data = Cobblemon.INSTANCE.getPlayerDataManager().getGenericData(player);
            boolean hasBand = data.getKeyItems().contains(dynamaxBandId);

            if (player.getUUID().equals(battleActor.getUuid())) {
                if (!hasBand && activeMovesets != null) {
                    for (ShowdownMoveset moveset : activeMovesets) {
                        moveset.blockGimmick(ShowdownMoveset.Gimmick.DYNAMAX);
                        moveset.setMaxMoves(null);
                    }
                }
            }
        }

        if (activeMovesets == null) {
            return;
        }

        List<ActiveBattlePokemon> activePokemon = battleActor.getActivePokemon();
        int activeCount = Math.min(activeMovesets.size(), activePokemon.size());

        for (int index = 0; index < activeCount; index++) {
            BattlePokemon battlePokemon = activePokemon.get(index).getBattlePokemon();
            if (battlePokemon != null
                    && !MegaGimmick.hasRequiredMegaFriendship(battlePokemon.getOriginalPokemon())) {
                activeMovesets.get(index).blockGimmick(ShowdownMoveset.Gimmick.MEGA_EVOLUTION);
            }
        }
    }
}
