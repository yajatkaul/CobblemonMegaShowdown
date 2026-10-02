package com.github.yajatkaul.mega_showdown.mixin.packet;

import com.cobblemon.mod.common.net.messages.server.storage.pc.MovePartyPokemonToPCPacket;
import com.cobblemon.mod.common.net.serverhandling.storage.pc.MovePartyPokemonToPCHandler;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.utils.PlayerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MovePartyPokemonToPCHandler.class, remap = false)
public class MovePartyPokemonToPCHandlerMixin {
    @Inject(
            method = "handle(Lcom/cobblemon/mod/common/net/messages/server/storage/pc/MovePartyPokemonToPCPacket;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At("TAIL")
    )
    private void onMove(MovePartyPokemonToPCPacket packet, MinecraftServer server, ServerPlayer player, CallbackInfo ci) {
        Pokemon pokemon = PlayerUtils.getPCPokemonFromUUID(player, packet.getPokemonID());

        if (pokemon != null && MegaGimmick.isMega(pokemon)) {
            MegaGimmick.megaToggle(pokemon);
        }
    }
}
