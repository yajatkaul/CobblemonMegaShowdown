package com.github.yajatkaul.mega_showdown.battle.messaging;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattlePokemonState;
import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattleStatePacketS2C;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class BattlePacketManager {
    private BattlePacketManager() {}

    public static void update (PokemonBattle battle) {
        BattleStatePacketS2C packet = createBattleState(battle);
        battle.getPlayers().forEach(packet::sendTo);
        battle.getSpectators()
            .stream()
            .map(PlayerExtensionsKt::getPlayer)
            .filter(Objects::nonNull)
            .forEach(packet::sendTo);
    }

    private static BattleStatePacketS2C createBattleState (PokemonBattle battle) {
        List<BattleSide> playerSides = Stream.of(battle.getSide1(), battle.getSide2())
            .filter(BattlePacketManager::isSideEligible)
            .toList();

        // TODO: there are other static constructors for different battle types, change this to whichever you want
        return BattleStatePacketS2C.of(BattlePokemonState::hidden, playerSides);
    }

    private static boolean isSideEligible (BattleSide side) {
        return Arrays.stream(side.getActors()).anyMatch(BattlePacketManager::isActorEligible);
    }

    private static boolean isActorEligible (BattleActor actor) {
        return true; // TODO: idk how you want to handle this
    }
}
