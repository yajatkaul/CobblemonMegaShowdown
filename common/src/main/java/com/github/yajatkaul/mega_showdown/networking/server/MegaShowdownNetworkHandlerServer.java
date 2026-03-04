package com.github.yajatkaul.mega_showdown.networking.server;

import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattleStatePacketS2C;
import com.github.yajatkaul.mega_showdown.networking.client.packet.ConfigSyncPacket;
import com.github.yajatkaul.mega_showdown.networking.client.packet.InteractionWheelPacket;
import com.github.yajatkaul.mega_showdown.networking.server.handler.MegaEvoHandler;
import com.github.yajatkaul.mega_showdown.networking.server.handler.SecretSwordMoveSwapHandler;
import com.github.yajatkaul.mega_showdown.networking.server.handler.UltraBurstHandler;
import com.github.yajatkaul.mega_showdown.networking.server.packet.MegaEvoPacket;
import com.github.yajatkaul.mega_showdown.networking.server.packet.SecretSwordMoveSwapPacket;
import com.github.yajatkaul.mega_showdown.networking.server.packet.UltraBurstPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public class MegaShowdownNetworkHandlerServer {
    public static void register() {
        if (Platform.getEnv() == EnvType.SERVER) {
            registerServerOnly();
        }

        registerCommon();
    }

    public static void registerServerOnly() {
        NetworkManager.registerS2CPayloadType(
                InteractionWheelPacket.TYPE,
                InteractionWheelPacket.STREAM_CODEC
        );
        NetworkManager.registerS2CPayloadType(
                ConfigSyncPacket.TYPE,
                ConfigSyncPacket.STREAM_CODEC
        );
        NetworkManager.registerS2CPayloadType(
                BattleStatePacketS2C.ID,
                BattleStatePacketS2C.PACKET_CODEC
        );
    }

    public static void registerCommon() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, MegaEvoPacket.TYPE, MegaEvoPacket.STREAM_CODEC, MegaEvoHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UltraBurstPacket.TYPE, UltraBurstPacket.STREAM_CODEC, UltraBurstHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SecretSwordMoveSwapPacket.TYPE, SecretSwordMoveSwapPacket.STREAM_CODEC, SecretSwordMoveSwapHandler::handle);
    }
}
