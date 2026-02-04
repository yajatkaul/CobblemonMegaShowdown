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

public class FormChangeInteractItem extends PokemonSelectingItem {
    private final String form_aspect_name;
    private final String form_aspect_apply;
    private final List<String> pokemons;
    private final String effectId;
    private final int consume;
    private final boolean revertable;
    private final String form_aspect_revert;

    public FormChangeInteractItem(Properties properties,
                                  String form_aspect_name,
                                  String form_aspect_apply,
                                  List<String> pokemons,
                                  String effectId,
                                  int consume,
                                  boolean revertable,
                                  String form_aspect_revert
    ) {
        super(properties);
        this.form_aspect_name = form_aspect_name;
        this.form_aspect_apply = form_aspect_apply;
        this.form_aspect_revert = form_aspect_revert;
        this.pokemons = pokemons;
        this.effectId = effectId;
        this.consume = consume;
        this.revertable = revertable;
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        if (!canUseOnPokemon(itemStack, pokemon)) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (pokemon.getAspects().contains(form_aspect_name) && !revertable) {
            return InteractionResultHolder.pass(itemStack);
        } else if (pokemon.getAspects().contains(form_aspect_name)) {
            Effect.getEffect(effectId).revertEffects(pokemon, List.of(form_aspect_revert), null);
            itemStack.consume(consume, serverPlayer);

            return InteractionResultHolder.success(itemStack);
        }

        Effect.getEffect(effectId).applyEffects(pokemon, List.of(form_aspect_apply), null);
        itemStack.consume(consume, serverPlayer);

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemons.contains(pokemon.getSpecies().getName()) && !pokemon.getPersistentData().contains("form_changing");
    }
}
