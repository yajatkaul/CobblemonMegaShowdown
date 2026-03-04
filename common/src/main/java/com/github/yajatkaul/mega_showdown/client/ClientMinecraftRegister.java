package com.github.yajatkaul.mega_showdown.client;

import com.github.yajatkaul.mega_showdown.client.battle.hud.BattleHud;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.player.LocalPlayer;

public class ClientMinecraftRegister {
    public static void register() {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((LocalPlayer player) -> MegaShowdownConfig.load());
        ClientGuiEvent.RENDER_HUD.register(BattleHud::hudCallback);

    }
}
