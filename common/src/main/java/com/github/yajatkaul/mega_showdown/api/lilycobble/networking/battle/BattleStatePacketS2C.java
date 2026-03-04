package com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unused")
public record BattleStatePacketS2C (List<String> fieldEffects, List<BattleSideState> sides) implements CustomPacketPayload {
    public static final Type<BattleStatePacketS2C> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "battle_state"));
    public static final BattleStatePacketS2C NULL_PACKET = new BattleStatePacketS2C(List.of(), List.of());

    public static final Codec<BattleStatePacketS2C> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.listOf().optionalFieldOf("field_effects", List.of()).forGetter(BattleStatePacketS2C::fieldEffects),
        BattleSideState.CODEC.listOf().optionalFieldOf("sides", List.of()).forGetter(BattleStatePacketS2C::sides)
    ).apply(instance, BattleStatePacketS2C::new));

    public static final StreamCodec<ByteBuf, BattleStatePacketS2C> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
        BattleStatePacketS2C::fieldEffects,
        BattleSideState.PACKET_CODEC.apply(ByteBufCodecs.list()),
        BattleStatePacketS2C::sides,
        BattleStatePacketS2C::new
    );

    public BattleStatePacketS2C (Set<String> fieldEffects, List<BattleSideState> sides) {
        this(fieldEffects.stream().toList(), sides);
    }

    public BattleStatePacketS2C (Collection<String> fieldEffects, BattleSideState... sides) {
        this(fieldEffects.stream().toList(), Arrays.stream(sides).toList());
    }

    public static BattleStatePacketS2C of (Function<BattlePokemon, BattlePokemonState> stateCreator, BattleSide... sides) {
        return of(stateCreator, Arrays.stream(sides).toList());
    }

    public static BattleStatePacketS2C of (Function<BattlePokemon, BattlePokemonState> stateCreator, List<BattleSide> sides) {
        if (sides.isEmpty()) return NULL_PACKET;

        Set<String> fieldEffects = getFieldEffects(sides.getFirst().getBattle());
        List<BattleSideState> sideStates = sides.stream().map(side -> BattleSideState.of(side, stateCreator)).toList();
        return new BattleStatePacketS2C(fieldEffects, sideStates);
    }

    public static BattleStatePacketS2C of (PokemonBattle battle) {
        return of(battle, BattlePokemonState::hidden);
    }

    public static BattleStatePacketS2C of (PokemonBattle battle, Function<BattlePokemon, BattlePokemonState> stateCreator) {
        return new BattleStatePacketS2C(
            getFieldEffects(battle),
            BattleSideState.of(battle.getSide1(), stateCreator),
            BattleSideState.of(battle.getSide2(), stateCreator)
        );
    }

    public static Set<String> getFieldEffects (PokemonBattle battle) {
        Set<String> fieldEffects = new HashSet<>();
        extractContext(fieldEffects, battle, BattleContext.Type.WEATHER);
        extractContext(fieldEffects, battle, BattleContext.Type.ROOM);
        extractContext(fieldEffects, battle, BattleContext.Type.TERRAIN);
        return fieldEffects;
    }

    public Optional<BattleSideState> getSide1 () {
        if (this.sides.isEmpty()) return Optional.empty();
        return Optional.of(this.sides.getFirst());
    }

    public Optional<BattleSideState> getSide2 () {
        if (this.sides.size() < 2) return Optional.empty();
        return Optional.of(this.sides.get(1));
    }

    public void sendTo (ServerPlayer player) {
        NetworkManager.sendToPlayer(player, this);
    }

    public ClientboundCustomPayloadPacket toPacket () {
        return new ClientboundCustomPayloadPacket(this);
    }

    private static void extractContext (Collection<String> mutableCollection, PokemonBattle battle, BattleContext.Type contextType) {
        Collection<BattleContext> fullContext = battle.getContextManager().get(contextType);
        if (fullContext == null) return;

        fullContext.stream()
            .map(BattleContext::getId)
            .forEach(mutableCollection::add);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type () {
        return ID;
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof BattleStatePacketS2C(List<String> otherEffects, List<BattleSideState> otherSides))) return false;
        return Objects.equals(this.fieldEffects, otherEffects) && Objects.equals(this.sides, otherSides);
    }

    @Override
    public int hashCode () {
        return Objects.hash(fieldEffects, sides);
    }

    @Override
    public @NotNull String toString () {
        return "BattleStatePacketS2C{" +
            "fieldEffects=" + this.fieldEffects +
            ", sides=" + this.sides +
            '}';
    }
}
