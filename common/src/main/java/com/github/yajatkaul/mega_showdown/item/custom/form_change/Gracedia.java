package com.github.yajatkaul.mega_showdown.item.custom.form_change;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.item.custom.PokemonSelectingBlockItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Gracedia extends PokemonSelectingBlockItem {
    public Gracedia(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        if (!canUseOnPokemon(itemStack, pokemon)) {
            return InteractionResultHolder.fail(itemStack);
        }

        long timeOfDay = serverPlayer.level().getDayTime() % 24000;
        boolean isDaytime = timeOfDay < 12000;
        boolean isSkyFormActive = pokemon.getAspects().contains("sky-forme");

        if (isDaytime && !isSkyFormActive) {
            Effect.getEffect("mega_showdown:shaymin_effect").applyEffects(pokemon, List.of("gracidea_forme=sky"), null);
        } else if (!isDaytime && isSkyFormActive) {
            Effect.getEffect("mega_showdown:shaymin_effect").applyEffects(pokemon, List.of("gracidea_forme=land"), null);
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemon.getSpecies().getName().equals("Shaymin") && !pokemon.getPersistentData().contains("form_changing");
    }
}
