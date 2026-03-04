package com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("unused")
public record BattleActorState (UUID uuid, List<BattlePokemonState> team) {
    public static final Codec<BattleActorState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("uuid").forGetter(BattleActorState::uuid),
        BattlePokemonState.CODEC.listOf().optionalFieldOf("team", List.of()).forGetter(BattleActorState::team)
    ).apply(instance, BattleActorState::new));

    public static final StreamCodec<ByteBuf, BattleActorState> PACKET_CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC,
        BattleActorState::uuid,
        BattlePokemonState.PACKET_CODEC.apply(ByteBufCodecs.list()),
        BattleActorState::team,
        BattleActorState::new
    );

    public static BattleActorState of (BattleActor actor, Function<BattlePokemon, BattlePokemonState> stateCreator) {
        List<BattlePokemonState> pokemon = actor.getPokemonList()
            .stream()
            .sorted(Comparator.comparing(pk -> pk.getEffectedPokemon().getDisplayName(false).getString()))
            .map(stateCreator)
            .toList();

        return new BattleActorState(actor.getUuid(), pokemon);
    }

    public boolean isFor (Player player) {
        return this.uuid.equals(player.getUUID());
    }

    public boolean isFor (NPCEntity npc) {
        return this.uuid.equals(npc.getUUID());
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof BattleActorState(UUID otherUUID, List<BattlePokemonState> otherTeam))) return false;
        return Objects.equals(this.uuid, otherUUID) && Objects.equals(this.team, otherTeam);
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.uuid, this.team);
    }

    @Override
    public @NotNull String toString () {
        return "BattleActorState{" +
            "uuid=" + this.uuid +
            ", team=" + this.team +
            '}';
    }
}
