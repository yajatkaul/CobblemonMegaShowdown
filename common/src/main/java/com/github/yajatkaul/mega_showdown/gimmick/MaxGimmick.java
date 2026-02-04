package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.AspectConditions;
import com.github.yajatkaul.mega_showdown.utils.GlowHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public record MaxGimmick(
        String pokemonShowdownId,
        String gmaxMove,
        AspectConditions aspectConditions
) {
    public static final Codec<MaxGimmick> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pokemon_showdown_id").forGetter(MaxGimmick::pokemonShowdownId),
            Codec.STRING.fieldOf("gmax_move").forGetter(MaxGimmick::gmaxMove),
            AspectConditions.CODEC.optionalFieldOf("aspect_conditions", AspectConditions.DEFAULT()).forGetter(MaxGimmick::aspectConditions)
    ).apply(instance, MaxGimmick::new));
    private static final Map<Pokemon, ScalingData> ACTIVE_SCALING_ANIMATIONS = new HashMap<>();
    private static final int DEFAULT_SCALING_DURATION = 60;

    public static void startGradualScaling(Pokemon entity, float targetScale) {
        if (entity == null) return;

        GlowHandler.applyDynamaxGlow(entity.getEntity());

        entity.getPersistentData().putFloat("orignal_size", entity.getScaleModifier());
        float startScale = entity.getScaleModifier();
        ScalingData scalingData = new ScalingData(startScale, targetScale, DEFAULT_SCALING_DURATION);

        ACTIVE_SCALING_ANIMATIONS.put(entity, scalingData);
    }

    public static void startGradualScalingDown(Pokemon pokemon) {
        if (pokemon == null) return;

        pokemon.getEntity().removeEffect(MobEffects.GLOWING);

        float startScale = pokemon.getScaleModifier();
        ScalingData scalingData = new ScalingData(startScale, pokemon.getPersistentData().getFloat("orignal_size"), DEFAULT_SCALING_DURATION);

        ACTIVE_SCALING_ANIMATIONS.put(pokemon, scalingData);
    }

    public static void updateScalingAnimations(MinecraftServer server) {
        if (server == null) return;

        Iterator<Map.Entry<Pokemon, ScalingData>> iterator = ACTIVE_SCALING_ANIMATIONS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Pokemon, ScalingData> entry = iterator.next();
            Pokemon pokemon = entry.getKey();
            ScalingData data = entry.getValue();
            data.currentTick++;

            if (pokemon.getEntity() == null) {
                iterator.remove();
                pokemon.setScaleModifier(pokemon.getPersistentData().getFloat("orignal_size"));
                continue;
            }

            float progress = Math.min(1.0f, (float) data.currentTick / data.durationTicks);
            float newScale = data.startScale + (data.targetScale - data.startScale) * progress;

            pokemon.setScaleModifier(newScale);

            if (data.currentTick >= data.durationTicks) {
                iterator.remove();
            }
        }
    }

    private static class ScalingData {
        final float startScale;
        final float targetScale;
        final int durationTicks;
        int currentTick;

        public ScalingData(float startScale, float targetScale, int durationTicks) {
            this.startScale = startScale;
            this.targetScale = targetScale;
            this.durationTicks = durationTicks;
            this.currentTick = 0;
        }
    }
}