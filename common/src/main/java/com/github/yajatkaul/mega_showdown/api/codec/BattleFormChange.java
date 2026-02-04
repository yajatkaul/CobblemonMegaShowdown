package com.github.yajatkaul.mega_showdown.api.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record BattleFormChange(
        List<String> pokemons,
        String showdownFormChangeId,
        AspectConditions aspects,
        ResourceLocation effect
) {
    public static final Codec<BattleFormChange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pokemons").forGetter(BattleFormChange::pokemons),
            Codec.STRING.fieldOf("showdown_form_change_id").forGetter(BattleFormChange::showdownFormChangeId),
            AspectConditions.CODEC.fieldOf("aspect_conditions").forGetter(BattleFormChange::aspects),
            ResourceLocation.CODEC.optionalFieldOf("effect", ResourceLocation.tryParse("")).forGetter(BattleFormChange::effect)
    ).apply(instance, BattleFormChange::new));
}