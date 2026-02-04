package com.github.yajatkaul.mega_showdown.item.custom.form_change;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.advancement.AdvancementHelper;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.item.custom.PokemonSelectingItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RotomCatalogue extends PokemonSelectingItem {
    private final List<String> form_apply_order = List.of(
            "heat-appliance",
            "fan-appliance",
            "mow-appliance",
            "frost-appliance",
            "wash-appliance",
            "none-appliance"
    );
    private final List<String> form_aspect_apply_order = List.of(
            "appliance=heat",
            "appliance=fan",
            "appliance=mow",
            "appliance=frost",
            "appliance=wash",
            "appliance=none"
    );
    private final List<String> effects_apply_order = List.of(
            "mega_showdown:rotom_heat_effect",
            "mega_showdown:rotom_fan_effect",
            "mega_showdown:rotom_mow_effect",
            "mega_showdown:rotom_frost_effect",
            "mega_showdown:rotom_wash_effect",
            "mega_showdown:rotom_effect"
    );

    public RotomCatalogue(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canUseOnPokemon(@NotNull ItemStack stack, @NotNull Pokemon pokemon) {
        return pokemon.getSpecies().getName().equals("Rotom") && !pokemon.getPersistentData().contains("form_changing");
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
            currentIndex = 5;
        }

        if (currentIndex + 1 > form_apply_order.size() - 1) {
            Effect.getEffect(effects_apply_order.getFirst()).applyEffects(pokemon, List.of(form_aspect_apply_order.getFirst()), null);
        } else {
            Effect.getEffect(effects_apply_order.get(currentIndex + 1)).applyEffects(pokemon, List.of(form_aspect_apply_order.get(currentIndex + 1)), null);
        }
        AdvancementHelper.grantAdvancement(serverPlayer, "rotom/rotom_form_change");

        return InteractionResultHolder.success(itemStack);
    }
}
