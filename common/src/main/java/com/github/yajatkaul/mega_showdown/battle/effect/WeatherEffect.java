package com.github.yajatkaul.mega_showdown.battle.effect;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.BattleEffect;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;
import net.minecraft.resources.ResourceLocation;

public class WeatherEffect extends AbstractFieldHandler {
    public static void handleWeather(PokemonBattle battle, String weatherName) {
        ResourceLocation id = getParticleId(weatherName);
        if (id == null) return;

        sendFieldEffect(id, battle);
    }

    private static ResourceLocation getParticleId(String weather) {
        for (BattleEffect battleEffect : MegaShowdownDatapackRegister.BATTLE_EFFECT_REGISTRY) {
            if (battleEffect.type().equals(BattleEffect.BattleEffectType.WEATHER)) {
                if (battleEffect.name().equals(weather)) {
                    return battleEffect.id();
                }
            }
        }

        return switch (weather) {
            case "raindance" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "weather_rain");
            case "sunnyday" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "sundrop");
            case "sandstorm" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "weather_sand");
            case "snow" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "weather_snow");
            case "desolateland" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "weather_desolateland");
            case "primordialsea" -> ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "weather_primordialsea");
            case null, default -> null;
        };
    }
}
