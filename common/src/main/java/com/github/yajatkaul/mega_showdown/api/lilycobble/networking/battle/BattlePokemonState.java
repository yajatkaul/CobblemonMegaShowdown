package com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;
import com.cobblemon.mod.common.api.pokemon.status.Status;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatusContainer;
import com.github.yajatkaul.mega_showdown.api.lilycobble.pokemon.PokemonPropertiesSupplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public record BattlePokemonState (
    UUID uuid,
    Optional<PokemonPropertiesSupplier> pokemonProperties,
    double healthPercentage,
    Optional<String> status,
    Optional<String> item,
    Map<String, Integer> statChanges,
    Optional<List<String>> moves
) {
    public static final Codec<BattlePokemonState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("uuid").forGetter(BattlePokemonState::uuid),
        PokemonPropertiesSupplier.CODEC.optionalFieldOf("pokemon_properties").forGetter(BattlePokemonState::pokemonProperties),
        Codec.DOUBLE.fieldOf("health_percentage").forGetter(BattlePokemonState::healthPercentage),
        Codec.STRING.optionalFieldOf("status").forGetter(BattlePokemonState::status),
        Codec.STRING.optionalFieldOf("item").forGetter(BattlePokemonState::item),
        Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("stat_changes").forGetter(BattlePokemonState::statChanges),
        Codec.STRING.listOf().optionalFieldOf("move").forGetter(BattlePokemonState::moves)
    ).apply(instance, BattlePokemonState::new));

    public static final StreamCodec<ByteBuf, BattlePokemonState> PACKET_CODEC = StreamCodec.composite(
        BattlePokemonStateP1.PACKET_CODEC,
        BattlePokemonStateP1::fromState,
        BattlePokemonStateP2.PACKET_CODEC,
        BattlePokemonStateP2::fromState,
        BattlePokemonState::fromPacket
    );

    /**
     * Creates a BattlePokemonState that hides all information not normally known to the player.
     * <p>
     * Equivalent to the standard Cobblemon experience.
     */
    public static BattlePokemonState hidden (BattlePokemon pokemon) {
        return of(pokemon, false, false, false, false);
    }

    /**
     * Creates a BattlePokemonState that includes the species and form of the Pokémon.
     * <p>
     * Equivalent to mainline Team Preview.
     */
    public static BattlePokemonState teamPreview (BattlePokemon pokemon) {
        return of(pokemon, true, false, false, false);
    }

    /**
     * Creates a BattlePokemonState that includes species, form, ability, moves, and item.
     * <p>
     * Equivalent to Open Team Sheet rules.
     */
    public static BattlePokemonState openTeamSheet (BattlePokemon pokemon) {
        return of(pokemon, true, true, true, true);
    }

    public static BattlePokemonState of (BattlePokemon pokemon, boolean includeProperties, boolean includeAbility, boolean includeMoves, boolean includeItem) {
        Map<String, Integer> statChanges = new HashMap<>();
        Collection<BattleContext> boosts = pokemon.getContextManager().get(BattleContext.Type.BOOST);
        Collection<BattleContext> unboosts = pokemon.getContextManager().get(BattleContext.Type.UNBOOST);

        if (boosts != null) {
            for (BattleContext boost : boosts) {
                statChanges.compute(boost.getId(),(key, value) -> (value == null ? 0 : value) + 1);
            }
        }

        if (unboosts != null) {
            for (BattleContext unboost : unboosts) {
                statChanges.compute(unboost.getId(),(key, value) -> (value == null ? 0 : value) - 1);
            }
        }

        Optional<PokemonPropertiesSupplier> properties;
        List<PokemonPropertyExtractor> extractors = new ArrayList<>();

        if (includeProperties) {
            extractors.add(PokemonPropertyExtractor.SPECIES);
            extractors.add(PokemonPropertyExtractor.FORM);
            extractors.add(PokemonPropertyExtractor.ASPECTS);
            extractors.add(PokemonPropertyExtractor.SHINY);
        }
        if (includeAbility) {
            extractors.add(PokemonPropertyExtractor.ABILITY);
        }

        if (extractors.isEmpty()) properties = Optional.empty();
        else properties = Optional.of(new PokemonPropertiesSupplier(pokemon.getEffectedPokemon().createPokemonProperties(extractors)));

        Optional<List<String>> moves;
        if (includeMoves) {
            moves = Optional.of(
                pokemon.getEffectedPokemon().getMoveSet().getMoves()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Move::getName)
                    .toList()
            );
        }
        else moves = Optional.empty();

        Optional<String> item;
        if (includeItem) item = Optional.ofNullable(pokemon.getHeldItemManager().showdownId(pokemon));
        else item = Optional.empty();

        return new BattlePokemonState(
            pokemon.getUuid(),
            properties,
            (double)pokemon.getHealth() / pokemon.getMaxHealth(),
            Optional.ofNullable(pokemon.getEffectedPokemon().getStatus())
                .map(PersistentStatusContainer::getStatus)
                .map(Status::getShowdownName),
            item,
            statChanges,
            moves
        );
    }

    private static BattlePokemonState fromPacket (BattlePokemonStateP1 part1, BattlePokemonStateP2 part2) {
        return new BattlePokemonState(part1.uuid, part1.properties, part1.healthPercentage, part2.status, part2.item, part2.statChanges, part2.moves);
    }

    public boolean isAlive () {
        return this.healthPercentage > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BattlePokemonState(
            UUID otherUUID,
            Optional<PokemonPropertiesSupplier> otherProperties,
            double otherPercentage,
            Optional<String> otherStatus,
            Optional<String> otherItem,
            Map<String, Integer> otherStats,
            Optional<List<String>> otherMoves
        ))) return false;
        return Double.compare(this.healthPercentage, otherPercentage) == 0
            && Objects.equals(this.uuid, otherUUID)
            && Objects.equals(this.status, otherStatus)
            && Objects.equals(this.item, otherItem)
            && Objects.equals(this.moves, otherMoves)
            && Objects.equals(this.statChanges, otherStats)
            && Objects.equals(this.pokemonProperties, otherProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid, this.pokemonProperties, this.healthPercentage, this.status, this.item, this.statChanges, this.moves);
    }

    private record BattlePokemonStateP1 (UUID uuid, Optional<PokemonPropertiesSupplier> properties, double healthPercentage) {
        private static final StreamCodec<ByteBuf, BattlePokemonStateP1> PACKET_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            BattlePokemonStateP1::uuid,
            ByteBufCodecs.optional(PokemonPropertiesSupplier.PACKET_CODEC),
            BattlePokemonStateP1::properties,
            ByteBufCodecs.DOUBLE,
            BattlePokemonStateP1::healthPercentage,
            BattlePokemonStateP1::new
        );

        private static BattlePokemonStateP1 fromState (BattlePokemonState state) {
            return new BattlePokemonStateP1(state.uuid, state.pokemonProperties, state.healthPercentage);
        }
    }

    private record BattlePokemonStateP2 (Optional<String> status, Optional<String> item, Map<String, Integer> statChanges, Optional<List<String>> moves) {
        private static final StreamCodec<ByteBuf, BattlePokemonStateP2> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            BattlePokemonStateP2::status,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            BattlePokemonStateP2::item,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT),
            BattlePokemonStateP2::statChanges,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())),
            BattlePokemonStateP2::moves,
            BattlePokemonStateP2::new
        );

        private static BattlePokemonStateP2 fromState (BattlePokemonState state) {
            return new BattlePokemonStateP2(state.status, state.item, state.statChanges, state.moves);
        }
    }

    @Override
    public @NotNull String toString () {
        return "BattlePokemonState{" +
            "uuid=" + this.uuid +
            ", pokemonProperties=" + this.pokemonProperties +
            ", healthPercentage=" + this.healthPercentage +
            ", status=" + this.status +
            ", item=" + this.item +
            ", statChanges=" + this.statChanges +
            ", moves=" + this.moves +
            '}';
    }
}
