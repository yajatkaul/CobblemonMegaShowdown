package com.github.yajatkaul.mega_showdown.networking.client;

import com.github.yajatkaul.mega_showdown.api.lilycobble.networking.battle.BattleStatePacketS2C;
import com.github.yajatkaul.mega_showdown.client.battle.hud.BattleHud;
import com.github.yajatkaul.mega_showdown.networking.client.handler.ConfigSyncHandler;
import com.github.yajatkaul.mega_showdown.networking.client.handler.InteractionWheelHandler;
import com.github.yajatkaul.mega_showdown.networking.client.packet.ConfigSyncPacket;
import com.github.yajatkaul.mega_showdown.networking.client.packet.InteractionWheelPacket;
import dev.architectury.networking.NetworkManager;

public class MegaShowdownNetworkHandlerClient {
    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, InteractionWheelPacket.TYPE, InteractionWheelPacket.STREAM_CODEC, InteractionWheelHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ConfigSyncPacket.TYPE, ConfigSyncPacket.STREAM_CODEC, ConfigSyncHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, BattleStatePacketS2C.ID, BattleStatePacketS2C.PACKET_CODEC, BattleHud::receivePacket);
    }
}
