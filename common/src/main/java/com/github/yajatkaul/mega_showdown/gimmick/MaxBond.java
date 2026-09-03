package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;

/**
 * Shared friendship rule for player-owned Pokémon using Mega Evolution or Z-Moves.
 * Non-player-owned Pokémon stay exempt so trainer and wild battle mechanics keep working.
 * <p>
 * The threshold comes from {@link MegaShowdownConfig#megaFriendshipRequirement}, so server owners
 * can tune it from config/mega_showdown/config.json without touching Cobblemon's own settings.
 */
public final class MaxBond {
    private MaxBond() {
    }

    public static boolean hasRequiredFriendship(Pokemon pokemon) {
        return pokemon != null
                && (!pokemon.isPlayerOwned()
                || pokemon.getFriendship() >= MegaShowdownConfig.megaFriendshipRequirement);
    }
}
