package com.github.yajatkaul.mega_showdown.item.custom.form_change;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.item.custom.PokemonSelectingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AshCap extends PokemonSelectingItem {
    public AshCap(Properties arg) {
        super(arg);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemon.getSpecies().getName().equals("Greninja") && !pokemon.getAspects().contains("bond");
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        if (!canUseOnPokemon(itemStack, pokemon)) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (pokemon.getFriendship() < MegaShowdownConfig.minBondingRequired) {
            serverPlayer.displayClientMessage(Component.translatable("message.mega_showdown.friendship_requirement")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.pass(itemStack);
        }
        Effect.getEffect("mega_showdown:heart_effect").applyEffects(pokemon, List.of("battle_bond=bond"), null);
        AdvancementHelper.grantAdvancement(serverPlayer, "bond/ash_cap_bond");
        itemStack.consume(1, serverPlayer);
        return InteractionResultHolder.success(itemStack);
    }
}
