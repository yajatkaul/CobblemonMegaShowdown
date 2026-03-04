package com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unused")
public record BattleSideState (List<String> sideEffects, List<BattleActorState> actors) {
    public static final Codec<BattleSideState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.listOf().optionalFieldOf("sided_effects", List.of()).forGetter(BattleSideState::sideEffects),
        BattleActorState.CODEC.listOf().optionalFieldOf("actors", List.of()).forGetter(BattleSideState::actors)
    ).apply(instance, BattleSideState::new));

    public static final StreamCodec<ByteBuf, BattleSideState> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
        BattleSideState::sideEffects,
        BattleActorState.PACKET_CODEC.apply(ByteBufCodecs.list()),
        BattleSideState::actors,
        BattleSideState::new
    );

    public static BattleSideState of (BattleSide side) {
        return of(side, BattlePokemonState::hidden);
    }

    public static BattleSideState of (BattleSide side, Function<BattlePokemon, BattlePokemonState> stateCreator) {
        Set<String> sidedEffects = new HashSet<>();
        extractContext(sidedEffects, side, BattleContext.Type.HAZARD);
        extractContext(sidedEffects, side, BattleContext.Type.SCREEN);
        extractContext(sidedEffects, side, BattleContext.Type.TAILWIND);
        extractContext(sidedEffects, side, BattleContext.Type.MISC);

        List<BattleActorState> actors = Arrays.stream(side.getActors())
            .sorted(Comparator.comparing(actor -> actor.getUuid().toString()))
            .map(actor -> BattleActorState.of(actor, stateCreator))
            .toList();

        return new BattleSideState(sidedEffects.stream().toList(), actors);
    }

    public List<BattlePokemonState> getPokemon () {
        return this.actors.stream()
            .flatMap(state -> state.team().stream())
            .toList();
    }

    public boolean isFor (Player player) {
        return this.actors.stream().anyMatch(actor -> actor.isFor(player));
    }

    public boolean isFor (NPCEntity npc) {
        return this.actors.stream().anyMatch(actor -> actor.isFor(npc));
    }

    private static void extractContext (Collection<String> mutableCollection, BattleSide side, BattleContext.Type contextType) {
        Collection<BattleContext> fullContext = side.getContextManager().get(contextType);
        if (fullContext == null) return;

        fullContext.stream()
            .map(BattleContext::getId)
            .forEach(mutableCollection::add);
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof BattleSideState(List<String> otherEffects, List<BattleActorState> otherActors))) return false;
        return Objects.equals(this.sideEffects, otherEffects) && Objects.equals(this.actors, otherActors);
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.sideEffects, this.actors);
    }

    @Override
    public @NotNull String toString () {
        return "BattleSideState{" +
            "sideEffects=" + this.sideEffects +
            ", actors=" + this.actors +
            '}';
    }
}
