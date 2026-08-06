package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.player.GeneralPlayerData;
import com.cobblemon.mod.common.battles.ActiveBattlePokemon;
import com.cobblemon.mod.common.battles.ShowdownActionRequest;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.battles.ShowdownPokemon;
import com.cobblemon.mod.common.battles.ShowdownSide;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.github.yajatkaul.mega_showdown.gimmick.GimmickTurnCheck;
import com.github.yajatkaul.mega_showdown.gimmick.MaxBond;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ShowdownActionRequest.class, remap = false)
public class ShowdownActionRequestMixin {
    @Shadow
    private List<ShowdownMoveset> active;

    @Shadow
    private ShowdownSide side;

    @Unique
    private BattleActor mega_showdown$battleActor;

    @Inject(method = "sanitize", at = @At("HEAD"), remap = false)
    private void beforeSanitize(PokemonBattle battle, BattleActor battleActor, CallbackInfo ci) {
        mega_showdown$battleActor = battleActor;
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

        mega_showdown$blockMaxBondGimmicksWithoutRequiredFriendship(battleActor);
    }

    @Inject(method = "saveToBuffer", at = @At("HEAD"), remap = false)
    private void beforeSaveToBuffer(RegistryFriendlyByteBuf buffer, CallbackInfo ci) {
        // The first request can be sanitized before BattleActor.activePokemon is populated.
        // Reapply the restriction immediately before the request is sent to the client.
        mega_showdown$blockMaxBondGimmicksWithoutRequiredFriendship(mega_showdown$battleActor);
    }

    @Unique
    private void mega_showdown$blockMaxBondGimmicksWithoutRequiredFriendship(BattleActor battleActor) {
        List<ShowdownMoveset> activeMovesets = active;
        if (battleActor == null || activeMovesets == null || activeMovesets.isEmpty()) {
            return;
        }

        List<ActiveBattlePokemon> actorActivePokemon = battleActor.getActivePokemon();
        List<BattlePokemon> requestActivePokemon = mega_showdown$getRequestActivePokemon(battleActor);

        for (int index = 0; index < activeMovesets.size(); index++) {
            BattlePokemon battlePokemon = null;
            if (index < actorActivePokemon.size()) {
                battlePokemon = actorActivePokemon.get(index).getBattlePokemon();
            }
            if (battlePokemon == null && index < requestActivePokemon.size()) {
                battlePokemon = requestActivePokemon.get(index);
            }
            if (battlePokemon != null
                    && !MaxBond.hasRequiredFriendship(battlePokemon.getOriginalPokemon())) {
                ShowdownMoveset moveset = activeMovesets.get(index);
                moveset.blockGimmick(ShowdownMoveset.Gimmick.MEGA_EVOLUTION);
                moveset.blockGimmick(ShowdownMoveset.Gimmick.Z_POWER);

                // canZMove owns the per-move gimmick mapping. Clear its stale mapping,
                // then restore Max Move mappings when Dynamax is also available.
                moveset.getMoves().forEach(move -> move.setGimmickMove(null));
                moveset.setGimmickMapping();
            }
        }
    }

    @Unique
    private List<BattlePokemon> mega_showdown$getRequestActivePokemon(BattleActor battleActor) {
        List<BattlePokemon> result = new ArrayList<>();
        if (side == null) {
            return result;
        }

        for (ShowdownPokemon showdownPokemon : side.getPokemon()) {
            if (!showdownPokemon.getActive()) {
                continue;
            }

            BattlePokemon matchingPokemon = battleActor.getPokemonList().stream()
                    .filter(pokemon -> pokemon.getUuid().equals(showdownPokemon.getUuid()))
                    .findFirst()
                    .orElse(null);
            if (matchingPokemon != null) {
                result.add(matchingPokemon);
            }
        }
        return result;
    }
}
