package com.github.yajatkaul.mega_showdown.item.custom.form_change;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.item.custom.PokemonSelectingItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FormChangeInteractToggleItem extends PokemonSelectingItem {
    private final List<String> form_apply_order;
    private final List<String> form_aspect_apply_order;
    private final List<String> pokemons;
    private final List<String> effectIds;
    private final int consume;

    public FormChangeInteractToggleItem(Properties properties,
                                        List<String> form_apply_order,
                                        List<String> form_aspect_apply_order,
                                        List<String> pokemons,
                                        List<String> effectIds,
                                        int consume
    ) {
        super(properties);
        this.form_apply_order = form_apply_order;
        this.form_aspect_apply_order = form_aspect_apply_order;
        this.pokemons = pokemons;
        this.effectIds = effectIds;
        this.consume = consume;
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        if (!canUseOnPokemon(itemStack, pokemon)) {
            return InteractionResultHolder.fail(itemStack);
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
            return InteractionResultHolder.pass(itemStack);
        }

        if (currentIndex + 1 > form_apply_order.size() - 1) {
            Effect.getEffect(effectIds.getFirst()).applyEffects(pokemon, List.of(form_aspect_apply_order.getFirst()), null);
        } else {
            Effect.getEffect(effectIds.get(currentIndex + 1)).applyEffects(pokemon, List.of(form_aspect_apply_order.get(currentIndex + 1)), null);
        }
        itemStack.consume(consume, serverPlayer);

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemons.contains(pokemon.getSpecies().getName()) && !pokemon.getPersistentData().contains("form_changing");
    }
}
