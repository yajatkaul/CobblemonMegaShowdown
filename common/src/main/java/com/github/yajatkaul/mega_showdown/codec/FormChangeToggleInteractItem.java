package com.github.yajatkaul.mega_showdown.codec;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.gimmick.codec.AspectConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record FormChangeToggleInteractItem(
        List<String> form_apply_order,
        Optional<List<AspectConditions>> aspect_conditions,
        List<List<String>> form_aspect_apply_order,
        List<String> pokemons,
        List<ResourceLocation> effects,
        int consume
) {
    public static final Codec<FormChangeToggleInteractItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("form_apply_order").forGetter(FormChangeToggleInteractItem::form_apply_order),
            AspectConditions.CODEC.listOf().optionalFieldOf("aspect_conditions").forGetter(FormChangeToggleInteractItem::aspect_conditions),
            Codec.STRING.listOf().listOf().fieldOf("form_aspect_apply_order").forGetter(FormChangeToggleInteractItem::form_aspect_apply_order),
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
                aspect_conditions.ifPresentOrElse((aspect_conditions -> {
                    if (aspect_conditions.getFirst().validate_apply(pokemon)) {
                        Effect.getEffect(effect).applyEffects(pokemon, form_aspect_apply_order.getFirst(), null);
                    }
                }), () -> Effect.getEffect(effect).applyEffects(pokemon, form_aspect_apply_order.getFirst(), null));
            } else {
                int finalCurrentIndex = currentIndex;
                ResourceLocation effect = effects.get(finalCurrentIndex + 1);
                aspect_conditions.ifPresentOrElse((aspect_conditions) -> {
                    if (aspect_conditions.get(finalCurrentIndex + 1).validate_apply(pokemon)) {
                        Effect.getEffect(effect).applyEffects(pokemon, form_aspect_apply_order.get(finalCurrentIndex + 1), null);
                    }
                }, () -> {
                    Effect.getEffect(effect).applyEffects(pokemon, form_aspect_apply_order.get(finalCurrentIndex + 1), null);
                });
            }
            stack.consume(consume, livingEntity);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
