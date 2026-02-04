package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.api.codec.ZCrystal;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class UltraGimmick {
    public static void ultraBurstToggle(Pokemon pokemon) {
        if (!MegaShowdownConfig.outSideUltraBurst || pokemon.getPersistentData().getBoolean("form_changing")) return;

        if (isUltra(pokemon)) {
            unUltraBurst(pokemon);
        } else if (canUltraBurst(pokemon)) {
            ultraBurst(pokemon);
        }
    }

    private static void ultraBurst(Pokemon pokemon) {
        if (pokemon.getAspects().contains("dawn-fusion")) {
            pokemon.getPersistentData().putString("necrozma_form", "prism_fusion=dawn");
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:ultra_burst"),
                    List.of("prism_fusion=dawn"),
                    pokemon,
                    "revert_aspects"
            );
        } else {
            pokemon.getPersistentData().putString("necrozma_form", "prism_fusion=dusk");
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:ultra_burst"),
                    List.of("prism_fusion=dusk"),
                    pokemon,
                    "revert_aspects"
            );
        }

        Effect.getEffect("mega_showdown:ultra_burst").applyEffects(pokemon, List.of("prism_fusion=ultra"), null);
        pokemon.setTradeable(false);
    }

    private static void unUltraBurst(Pokemon pokemon) {
        String org_form = pokemon.getPersistentData().getString("necrozma_form");
        pokemon.getPersistentData().remove("necrozma_form");

        Effect.getEffect("mega_showdown:ultra_burst").revertEffects(pokemon, List.of(org_form), null);
        pokemon.setTradeable(true);
    }

    public static void ultraBurstInBattle(Pokemon pokemon, BattlePokemon battlePokemon) {
        if (pokemon.getAspects().contains("dawn-fusion")) {
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:ultra_burst"),
                    List.of("prism_fusion=dawn"),
                    pokemon,
                    "battle_end_revert"
            );
        } else {
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:ultra_burst"),
                    List.of("prism_fusion=dusk"),
                    pokemon,
                    "battle_end_revert"
            );
        }

        Effect.getEffect("mega_showdown:ultra_burst").applyEffectsBattle(pokemon, List.of("prism_fusion=ultra"), null, battlePokemon);
    }

    public static boolean canUltraBurst(Pokemon pokemon) {
        ServerPlayer player = pokemon.getOwnerPlayer();
        ItemStack heldItem = pokemon.heldItem();

        ZCrystal zCrystal = RegistryLocator.getComponent(ZCrystal.class, heldItem);

        if (player != null && !GimmickTurnCheck.hasGimmick(ShowdownMoveset.Gimmick.Z_POWER, player)) return false;

        if (zCrystal == null) return false;

        if (pokemon.getSpecies().getName().equals("Necrozma") && zCrystal.showdown_item_id().equals("ultranecroziumz")) {
            return pokemon.getAspects().contains("dawn-fusion") || pokemon.getAspects().contains("dusk-fusion");
        }
        return false;
    }

    public static boolean isUltra(Pokemon pokemon) {
        return pokemon.getAspects().contains("ultra-fusion");
    }
}
