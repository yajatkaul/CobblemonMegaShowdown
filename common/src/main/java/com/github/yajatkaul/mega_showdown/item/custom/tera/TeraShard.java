package com.github.yajatkaul.mega_showdown.item.custom.tera;

import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import com.github.yajatkaul.mega_showdown.item.custom.MSDPokemonSelectingItem;
import com.github.yajatkaul.mega_showdown.utils.ParticlesList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeraShard extends MSDPokemonSelectingItem {
    private final TeraType teraType;

    public TeraShard(Properties arg, TeraType teraType) {
        super(arg);
        this.teraType = teraType;
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer player, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        if (pokemon.getSpecies().getName().equals("Ogerpon") ||
                !pokemon.getSpecies().getName().equals("Terapagos") ||
                pokemon.getTeraType() == teraType) {
            return InteractionResultHolder.pass(itemStack);
        }

        final int required_shards = MegaShowdownConfig.teraShardRequired;

        if (itemStack.getCount() >= required_shards) {
            itemStack.consume(required_shards, player);

            if (itemStack.getItem() == MegaShowdownItems.STELLAR_TERA_SHARD.get()) {
                AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "tera/change_tera_stellar");
            }

            if (pokemon.getEntity() != null) {
                ParticlesList.glowParticles.apply(pokemon.getEntity());
            }

            pokemon.setTeraType(teraType);
            pokemon.setTeraType(teraType);
            AdvancementHelper.grantAdvancement(pokemon.getOwnerPlayer(), "tera/change_tera");
        } else {
            player.displayClientMessage(Component.translatable("message.mega_showdown.tera_requirements", required_shards)
                    .withColor(0xFF0000), true);
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return !pokemon.getSpecies().getName().equals("Ogerpon") &&
                !pokemon.getSpecies().getName().equals("Terapagos") &&
                pokemon.getTeraType() != teraType;
    }
}
