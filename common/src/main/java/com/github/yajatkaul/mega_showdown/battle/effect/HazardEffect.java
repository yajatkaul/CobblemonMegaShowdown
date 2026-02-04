package com.github.yajatkaul.mega_showdown.battle.effect;

import com.cobblemon.mod.common.battles.BattleSide;
import com.github.yajatkaul.mega_showdown.api.codec.BattleEffect;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;

public class HazardEffect extends AbstractSideHandler {
    public static void handleHazard(BattleSide side, String hazardName, int ticks) {
        EffectWrapper effect = getEffect(hazardName);
        if (effect == null || ticks % effect.tickInterval() != 0) return;

        side.getActivePokemon().forEach(pokemon -> {
            if (pokemon.getBattlePokemon() != null && pokemon.getBattlePokemon().getEntity() != null) {
                sendEntityEffect(side.getBattle(), effect, pokemon.getBattlePokemon().getEntity(), "root");
            }
        });
    }

    private static EffectWrapper getEffect(String hazard) {
        for (BattleEffect battleEffect : MegaShowdownDatapackRegister.BATTLE_EFFECT_REGISTRY) {
            if (battleEffect.type().equals(BattleEffect.BattleEffectType.HAZARD)) {
                if (battleEffect.name().equals(hazard)) {
                    return new EffectWrapper(battleEffect.tickInterval(), battleEffect.id());
                }
            }
        }

        return switch (hazard) {
            case "stealthrock" -> EffectWrapper.STEALTH_ROCKS;
            case "stickyweb" -> EffectWrapper.STICKY_WEB;
            case "spikes" -> EffectWrapper.SPIKES;
            case "toxicspikes" -> EffectWrapper.TOXIC_SPIKES;
            case null, default -> null;
        };
    }
}
