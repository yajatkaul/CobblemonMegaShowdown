package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.api.codec.AspectConditions;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record MegaGimmick(
        String showdown_id,
        List<String> pokemons,
        AspectConditions aspect_conditions
) {
    public static final Codec<MegaGimmick> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("showdown_id").forGetter(MegaGimmick::showdown_id),
            Codec.list(Codec.STRING).fieldOf("pokemons").forGetter(MegaGimmick::pokemons),
            AspectConditions.CODEC.fieldOf("aspect_conditions").forGetter(MegaGimmick::aspect_conditions)
    ).apply(instance, MegaGimmick::new));
    public static final String IS_MEGA_TAG = "is_mega";

    public static boolean isMega(Pokemon pokemon) {
        return pokemon.getPersistentData().getBoolean(IS_MEGA_TAG);
    }

    public static boolean hasMega(ServerPlayer player) {
        if (MegaShowdownConfig.multipleMegas) {
            return false;
        }

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        PCStore pcStore = Cobblemon.INSTANCE.getStorage().getPC(player);

        for (Pokemon pokemon : playerPartyStore) {
            if (pokemon.getPersistentData().getBoolean(IS_MEGA_TAG)) {
                return true;
            }
        }

        for (Pokemon pokemon : pcStore) {
            if (pokemon.getPersistentData().getBoolean(IS_MEGA_TAG)) {
                return true;
            }
        }

        return false;
    }

    public static void megaEvolveInBattle(Pokemon pokemon, BattlePokemon battlePokemon) {
        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (megaGimmick != null || pokemon.getSpecies().getName().equals("Rayquaza")) {
            if (pokemon.getSpecies().getName().equals("Rayquaza")) {
                Effect.getEffect("mega_showdown:mega_evolution").applyEffectsBattle(pokemon, List.of("mega_evolution=mega"), null, battlePokemon);

                AspectUtils.appendRevertDataPokemon(
                        Effect.getEffect("mega_showdown:mega_evolution"),
                        List.of("mega_evolution=none"),
                        pokemon,
                        "battle_end_revert"
                );
            } else {
                Effect.getEffect("mega_showdown:mega_evolution").applyEffectsBattle(pokemon, megaGimmick.aspect_conditions.aspectApply().aspects(), null, battlePokemon);

                AspectUtils.appendRevertDataPokemon(
                        Effect.getEffect("mega_showdown:mega_evolution"),
                        megaGimmick.aspect_conditions.aspectRevert().aspects(),
                        pokemon,
                        "battle_end_revert"
                );
            }
        }
    }

    public static void megaToggle(Pokemon pokemon) {
        if (!MegaShowdownConfig.outSideMega || pokemon == null || pokemon.getPersistentData().getBoolean("form_changing")) {
            return;
        }
        if (pokemon.getPersistentData().getBoolean(IS_MEGA_TAG)) {
            unmegaEvolve(pokemon);
        } else {
            AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "mega/mega_evolve");
            megaEvolve(pokemon);
        }
    }

    public static void megaEvolve(Pokemon pokemon) {
        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (pokemon.getSpecies().getName().equals("Rayquaza")) {
            Effect.getEffect("mega_showdown:mega_evolution").applyEffects(pokemon, List.of("mega_evolution=mega"), null);
        } else if (megaGimmick != null) {
            Effect.getEffect("mega_showdown:mega_evolution").applyEffects(pokemon, megaGimmick.aspect_conditions.aspectApply().aspects(), null);
        }
        pokemon.getPersistentData().putBoolean(IS_MEGA_TAG, true);
        pokemon.setTradeable(false);
    }

    public static void unmegaEvolve(Pokemon pokemon) {
        pokemon.getPersistentData().remove(IS_MEGA_TAG);
        Effect.getEffect("mega_showdown:mega_evolution").revertEffects(pokemon, List.of("mega_evolution=none"), null);
        pokemon.setTradeable(true);
    }

    public static boolean canMega(Pokemon pokemon) {
        ServerPlayer player = pokemon.getOwnerPlayer();

        if (player != null && !GimmickTurnCheck.hasGimmick(ShowdownMoveset.Gimmick.MEGA_EVOLUTION, player)) {
            return false;
        }

        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (!pokemon.getSpecies().getName().equals("Rayquaza") && megaGimmick == null) return false;

        if (player != null && hasMega(player)) {
            return false;
        }

        if (pokemon.getSpecies().getName().equals("Rayquaza")) {
            for (int i = 0; i < 4; i++) {
                if (pokemon.getMoveSet().getMoves().get(i).getName().equals("dragonascent")) {
                    return true;
                }
            }
        }

        if (megaGimmick == null) return false;

        if (!megaGimmick.aspect_conditions.validate_apply(pokemon)) {
            return false;
        }

        return megaGimmick.pokemons.contains(pokemon.getSpecies().getName());
    }
}
