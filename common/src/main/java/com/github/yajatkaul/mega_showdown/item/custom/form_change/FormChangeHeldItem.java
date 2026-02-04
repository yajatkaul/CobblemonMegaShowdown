package com.github.yajatkaul.mega_showdown.item.custom.form_change;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.item.custom.ToolTipItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FormChangeHeldItem extends ToolTipItem {
    private final String revertAspect;
    private final String applyAspect;
    private final List<String> pokemons;
    private final String effectId;
    private final boolean tradable;
    private final @Nullable Consumer<Pokemon> onApplyCallback;
    private final @Nullable Consumer<Pokemon> onRevertCallback;

    public FormChangeHeldItem(Properties properties, String revertAspect, String applyAspect, List<String> pokemons, String effectId, boolean tradable, @Nullable Consumer<Pokemon> onApplyCallback, @Nullable Consumer<Pokemon> onRevertCallback) {
        super(properties);
        this.revertAspect = revertAspect;
        this.applyAspect = applyAspect;
        this.pokemons = pokemons;
        this.effectId = effectId;
        this.tradable = tradable;
        this.onApplyCallback = onApplyCallback;
        this.onRevertCallback = onRevertCallback;
    }

    public void apply(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName())) {
            if (onApplyCallback != null) {
                onApplyCallback.accept(pokemon);
            }
            Effect.getEffect(effectId).applyEffects(pokemon, List.of(applyAspect), null);
            if (!tradable) {
                pokemon.setTradeable(false);
            }
        }
    }

    public void revert(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName())) {
            if (onRevertCallback != null) {
                onRevertCallback.accept(pokemon);
            }
            Effect.getEffect(effectId).revertEffects(pokemon, List.of(revertAspect), null);
            if (!tradable) {
                pokemon.setTradeable(true);
            }
        }
    }
}
