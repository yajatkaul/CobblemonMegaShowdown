package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AspectConditions(
        Conditions aspectApply,
        Conditions aspectRevert
) {
    public static final Codec<AspectConditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Conditions.CODEC.optionalFieldOf("apply", Conditions.DEFAULT()).forGetter(AspectConditions::aspectApply),
            Conditions.CODEC.optionalFieldOf("revert", Conditions.DEFAULT()).forGetter(AspectConditions::aspectRevert)
    ).apply(instance, AspectConditions::new));

    public boolean validate_apply(Pokemon pokemon) {
        return aspectApply.validate(pokemon);
    }

    public boolean validate_revert(Pokemon pokemon) {
        return aspectRevert.validate(pokemon);
    }

    public static AspectConditions DEFAULT() {
        return new AspectConditions(
                Conditions.DEFAULT(),
                Conditions.DEFAULT()
        );
    }
}
