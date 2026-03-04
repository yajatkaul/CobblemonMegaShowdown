package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.client.net.battle.BattleMessageHandler;
import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import com.github.yajatkaul.mega_showdown.client.battle.hud.BattleHud;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BattleMessageHandler.class)
public class BattleMessagePacketHandlerMixin {
    @Inject(method = "handle(Lcom/cobblemon/mod/common/net/messages/client/battle/BattleMessagePacket;Lnet/minecraft/client/Minecraft;)V", at = @At("TAIL"))
    private void grabMessage (BattleMessagePacket packet, Minecraft client, CallbackInfo info) {
        BattleHud.messageCallback(packet.getMessages());
    }
}
