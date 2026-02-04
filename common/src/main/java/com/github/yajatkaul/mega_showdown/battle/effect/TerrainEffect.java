package com.github.yajatkaul.mega_showdown.battle.effect;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.battles.ActiveBattlePokemon;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TerrainEffect extends AbstractFieldHandler {
    public static void handleTerrain(PokemonBattle battle, String terrainName, int ticks) {
        ElementalType type = getTypeForTerrain(terrainName);
        if (type == null) return;

        List<Vec3> pokemonPositions = new ArrayList<>();
        ServerLevel level = null;

        for (ActiveBattlePokemon pokemon : battle.getActivePokemon()) {
            if (pokemon.getPosition() != null) {
                pokemonPositions.add(pokemon.getPosition().getSecond());
                level = pokemon.getPosition().getFirst();
            }
        }

        if (pokemonPositions.isEmpty() || level == null) return;

        Vec3 centre = pokemonPositions.stream().reduce(Vec3.ZERO, Vec3::add).scale(1.0 / pokemonPositions.size());
        double maxDistance = 0;
        for (Vec3 position : pokemonPositions) {
            for (Vec3 otherPosition : pokemonPositions) {
                if (position.distanceTo(otherPosition) > maxDistance) maxDistance = position.distanceTo(otherPosition);
            }
        }
        maxDistance += 2;

        List<ServerPlayer> players = Stream.concat(
                battle.getPlayers().stream(),
                battle.getSpectators().stream()
                        .map(level::getPlayerByUUID)
                        .filter(player -> player instanceof ServerPlayer)
                        .map(player -> (ServerPlayer) player)
        ).toList();
        for (int i = 1; i <= maxDistance; ++i) {
            createTerrainRing(level, players, centre, i, type);
        }
    }

    @Nullable
    private static ElementalType getTypeForTerrain(String terrain) {
        return switch (terrain) {
            case "mistyterrain" -> ElementalTypes.FAIRY;
            case "electricterrain" -> ElementalTypes.ELECTRIC;
            case "grassyterrain" -> ElementalTypes.GRASS;
            case "psychicterrain" -> ElementalTypes.PSYCHIC;
            default -> null;
        };
    }

    private static void createTerrainRing(ServerLevel level, List<ServerPlayer> viewers, Vec3 centre, double distance, ElementalType type) {
        for (float i = 0; i < 2 * Mth.PI; i += (float) Math.PI / (8f * (float) distance)) {
            Vec3 particlePos = centre.add(Mth.cos(i) * distance, 0, Mth.sin(i) * distance);
            viewers.forEach(viewer -> level.sendParticles(
                    viewer,
                    getDustParticleForType(type, 1.5f),
                    false,
                    particlePos.x(),
                    particlePos.y(),
                    particlePos.z(),
                    1,
                    0,
                    0,
                    0,
                    0
            ));
        }
    }

    static ScalableParticleOptionsBase getDustParticleForType(ElementalType type, float scale) {
        return new DustParticleOptions(Vec3.fromRGB24(type.getHue()).toVector3f(), scale);
    }
}
