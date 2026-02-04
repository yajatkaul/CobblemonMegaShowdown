package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record FormChangeInteractItem(
        AspectConditions aspect_conditions,
        List<String> pokemons,
        Optional<ResourceLocation> effect,
        int consume
) {
    public static final Codec<FormChangeInteractItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AspectConditions.CODEC.fieldOf("aspect_conditions").forGetter(FormChangeInteractItem::aspect_conditions),
            Codec.STRING.listOf().fieldOf("pokemons").forGetter(FormChangeInteractItem::pokemons),
            ResourceLocation.CODEC.optionalFieldOf("effect").forGetter(FormChangeInteractItem::effect),
            Codec.INT.optionalFieldOf("consume", 0).forGetter(FormChangeInteractItem::consume)
    ).apply(instance, FormChangeInteractItem::new));

    public InteractionResult interactLivingEntity(Player player, LivingEntity livingEntity, ItemStack stack) {
        if (player.level().isClientSide || player.isCrouching()) {
            return InteractionResult.PASS;
        }

        if (livingEntity instanceof PokemonEntity pokemonEntity) {
            Pokemon pokemon = pokemonEntity.getPokemon();

            if (!pokemons.contains(pokemon.getSpecies().getName()) ||
                    pokemon.getOwnerPlayer() != player ||
                    pokemonEntity.isBattling() ||
                    pokemonEntity.getTethering() != null ||
                    pokemon.getPersistentData().contains("form_changing")) {
                return InteractionResult.PASS;
            }

            if (aspect_conditions.validate_apply(pokemon)) {
                Effect.getEffect(effect.get()).applyEffects(pokemon, aspect_conditions.aspectApply().aspects(), null);
                stack.consume(consume, livingEntity);
                return InteractionResult.SUCCESS;
            } else if (aspect_conditions.validate_revert(pokemon)) {
                Effect.getEffect(effect.get()).revertEffects(pokemon, aspect_conditions.aspectRevert().aspects(), null);
                stack.consume(consume, livingEntity);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
