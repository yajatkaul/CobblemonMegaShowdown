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

public record FormChangeToggleInteractItem(
        List<String> form_apply_order,
        List<AspectConditions> aspect_conditions,
        List<String> pokemons,
        List<ResourceLocation> effects,
        int consume
) {
    public static final Codec<FormChangeToggleInteractItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("form_apply_order").forGetter(FormChangeToggleInteractItem::form_apply_order),
            AspectConditions.CODEC.listOf().fieldOf("aspect_conditions").forGetter(FormChangeToggleInteractItem::aspect_conditions),
            Codec.STRING.listOf().fieldOf("pokemons").forGetter(FormChangeToggleInteractItem::pokemons),
            ResourceLocation.CODEC.listOf().fieldOf("effects").forGetter(FormChangeToggleInteractItem::effects),
            Codec.INT.optionalFieldOf("consume", 0).forGetter(FormChangeToggleInteractItem::consume)
    ).apply(instance, FormChangeToggleInteractItem::new));

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

            int currentIndex = -1;
            for (int i = 0; i < form_apply_order.size(); i++) {
                String form = form_apply_order.get(i);
                boolean hasAspect = pokemon.getAspects().stream().anyMatch(aspect -> aspect.equalsIgnoreCase(form));
                if (hasAspect) {
                    currentIndex = i;
                    break;
                }
            }

            if (currentIndex == -1) {
                return InteractionResult.PASS;
            }

            if (currentIndex + 1 > form_apply_order.size() - 1) {
                ResourceLocation effect = effects.getFirst();
                if (aspect_conditions.getFirst().validate_apply(pokemon)) {
                    Effect.getEffect(effect).applyEffects(pokemon, aspect_conditions.getFirst().aspectApply().aspects(), null);
                }
            } else {
                int nextIndex = currentIndex + 1;
                ResourceLocation effect = effects.get(nextIndex);
                if (aspect_conditions.get(nextIndex).validate_apply(pokemon)) {
                    Effect.getEffect(effect).applyEffects(pokemon, aspect_conditions.get(nextIndex).aspectApply().aspects(), null);
                }
            }
            stack.consume(consume, livingEntity);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
