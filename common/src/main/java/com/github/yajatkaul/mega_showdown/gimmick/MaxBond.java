package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;

/**
 * Shared maximum-friendship rule for player-owned Pokémon using Mega Evolution or Z-Moves.
 * Non-player-owned Pokémon stay exempt so trainer and wild battle mechanics keep working.
 */
public final class MaxBond {
    private MaxBond() {
    }

    public static boolean hasRequiredFriendship(Pokemon pokemon) {
        return pokemon != null
                && (!pokemon.isPlayerOwned()
                || pokemon.getFriendship() >= Cobblemon.INSTANCE.getConfig().getMaxPokemonFriendship());
    }
}
