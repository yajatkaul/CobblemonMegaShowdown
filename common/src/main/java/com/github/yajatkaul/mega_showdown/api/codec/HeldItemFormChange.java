package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record HeldItemFormChange(
        List<String> pokemons,
        AspectConditions aspect_conditions,
        Optional<ResourceLocation> effect,
        boolean tradable
) {
    public static final Codec<HeldItemFormChange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pokemons").forGetter(HeldItemFormChange::pokemons),
            AspectConditions.CODEC.fieldOf("aspect_conditions").forGetter(HeldItemFormChange::aspect_conditions),
            ResourceLocation.CODEC.optionalFieldOf("effect").forGetter(HeldItemFormChange::effect),
            Codec.BOOL.optionalFieldOf("tradable", true).forGetter(HeldItemFormChange::tradable)
    ).apply(instance, HeldItemFormChange::new));

    public void apply(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName()) && aspect_conditions().validate_apply(pokemon)) {
            Effect.getEffect(effect.get()).applyEffects(pokemon, aspect_conditions.aspectApply().aspects(), null);
            if (!tradable) {
                pokemon.setTradeable(false);
            }
        }
    }

    public void revert(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName()) && aspect_conditions().validate_revert(pokemon)) {
            Effect.getEffect(effect.get()).revertEffects(pokemon, aspect_conditions.aspectRevert().aspects(), null);
            if (!tradable) {
                pokemon.setTradeable(true);
            }
        }
    }
}
