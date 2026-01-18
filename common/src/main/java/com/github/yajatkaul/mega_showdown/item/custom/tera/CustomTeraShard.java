package com.github.yajatkaul.mega_showdown.item.custom.tera;

import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.item.custom.PokemonSelectingItemNoToolTip;
import com.github.yajatkaul.mega_showdown.utils.ParticlesList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomTeraShard extends PokemonSelectingItemNoToolTip {
    public CustomTeraShard(Properties arg) {
        super(arg);
    }

    @Override
    public @Nullable InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer player, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        TeraType teraType = itemStack.get(MegaShowdownDataComponents.TERA_TYPE.get());
        if (teraType == null) return InteractionResultHolder.pass(itemStack);

        final int required_shards = MegaShowdownConfig.teraShardRequired;

        if (itemStack.getCount() >= required_shards) {
            itemStack.consume(required_shards, player);


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
        TeraType teraType = stack.get(MegaShowdownDataComponents.TERA_TYPE.get());
        if (teraType == null) return false;

        return !pokemon.getSpecies().getName().equals("Ogerpon") &&
                !pokemon.getSpecies().getName().equals("Terapagos") &&
                pokemon.getTeraType() != teraType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TeraType teraType = itemStack.get(MegaShowdownDataComponents.TERA_TYPE.get());
        if (teraType == null) {
            super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.msd.custom_tera_shard" + teraType.showdownId() + ".tooltip"));
        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}
