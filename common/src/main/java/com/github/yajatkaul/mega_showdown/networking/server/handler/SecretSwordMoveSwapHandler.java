package com.github.yajatkaul.mega_showdown.networking.server.handler;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.networking.server.packet.SecretSwordMoveSwapPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class SecretSwordMoveSwapHandler {
    public static void handle(SecretSwordMoveSwapPacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();

        if (player instanceof ServerPlayer serverPlayer) {
            for (Pokemon pokemon : Cobblemon.INSTANCE.getStorage().getParty(serverPlayer)) {
                if (!pokemon.getSpecies().getName().equals("Keldeo")) {
                    continue;
                }

                boolean hasSecretSword = pokemon.getMoveSet()
                        .getMoves()
                        .stream()
                        .anyMatch(move -> move.getTemplate().getName().equals("secretsword"));
                boolean isResolute = pokemon.getAspects().contains("resolute-form");

                if (!isResolute && hasSecretSword) {
                    Effect.getEffect("mega_showdown:keldeo_effect").applyEffects(pokemon, List.of("sword_form=resolute"), null);
                } else if (isResolute && !hasSecretSword) {
                    Effect.getEffect("mega_showdown:keldeo_effect").revertEffects(pokemon, List.of("sword_form=ordinary"), null);
                }
            }
        }
    }
}
