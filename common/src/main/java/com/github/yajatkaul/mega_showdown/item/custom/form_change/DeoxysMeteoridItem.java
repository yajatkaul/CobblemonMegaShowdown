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

public class DeoxysMeteoridItem extends PokemonSelectingBlockItem {
    private final List<String> form_apply_order = List.of("normal-forme", "attack-forme", "speed-forme", "defense-forme");
    private final List<List<String>> form_aspect_apply_order = List.of(
            List.of("meteorite_forme=normal"),
            List.of("meteorite_forme=attack"),
            List.of("meteorite_forme=speed"),
            List.of("meteorite_forme=defense")
    );

    public DeoxysMeteoridItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemon.getSpecies().getName().equals("Deoxys") && !pokemon.getPersistentData().contains("form_changing");
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
            Effect.getEffect("mega_showdown:deoxys_effect").applyEffects(pokemon, form_aspect_apply_order.getFirst(), null);
        } else {
            Effect.getEffect("mega_showdown:deoxys_effect").applyEffects(pokemon, form_aspect_apply_order.get(currentIndex + 1), null);
        }

        return InteractionResultHolder.success(itemStack);
    }
}
