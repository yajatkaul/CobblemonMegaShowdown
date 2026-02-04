package com.github.yajatkaul.mega_showdown.api.codec.particles;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.utils.PokemonBehaviourHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kotlin.Unit;

import java.util.HashSet;
import java.util.List;

public record AnimationData(
        List<String> animations_apply,
        List<String> expressions_apply,
        List<String> animations_revert,
        List<String> expressions_revert,
        float applyDelay,
        float revertDelay
) {
    public static final Codec<AnimationData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).optionalFieldOf("animations_apply", List.of()).forGetter(AnimationData::animations_apply),
            Codec.list(Codec.STRING).optionalFieldOf("expressions_apply", List.of()).forGetter(AnimationData::expressions_apply),
            Codec.list(Codec.STRING).optionalFieldOf("animations_revert", List.of()).forGetter(AnimationData::animations_revert),
            Codec.list(Codec.STRING).optionalFieldOf("expressions_revert", List.of()).forGetter(AnimationData::expressions_revert),
            Codec.FLOAT.optionalFieldOf("apply_delay", 0f).forGetter(AnimationData::applyDelay),
            Codec.FLOAT.optionalFieldOf("revert_delay", 0f).forGetter(AnimationData::revertDelay)
    ).apply(instance, AnimationData::new));

    public void applyAnimations(PokemonEntity context) {
        context.after(applyDelay, () -> {
            PokemonBehaviourHelper.Companion.playAnimation(context, new HashSet<>(this.animations_apply), this.expressions_apply);
            return Unit.INSTANCE;
        });
    }

    public void revertAnimations(PokemonEntity context) {
        context.after(revertDelay, () -> {
            PokemonBehaviourHelper.Companion.playAnimation(context, new HashSet<>(this.animations_revert), this.expressions_revert);
            return Unit.INSTANCE;
        });
    }
}