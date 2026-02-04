package com.github.yajatkaul.mega_showdown.item.custom.z;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.FormChangeHeldItem;

import java.util.List;
import java.util.Locale;

public class ElementalZCrystal extends FormChangeHeldItem {
    private final ElementalType element;
    private final List<String> pokemons;
    private final boolean tradable;

    public ElementalZCrystal(Properties properties,
                             List<String> pokemons,
                             boolean tradable,
                             ElementalType element
    ) {
        super(properties, null, null, pokemons, null, tradable, null, null);
        this.element = element;
        this.pokemons = pokemons;
        this.tradable = tradable;
    }

    @Override
    public void apply(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName())) {
            String element = this.element.getName().toLowerCase(Locale.ROOT);
            Effect.getEffect("mega_showdown:arceus_" + element).applyEffects(pokemon, List.of(String.format("multitype=%s", element)), null);
            if (!tradable) {
                pokemon.setTradeable(false);
            }
        }
    }

    @Override
    public void revert(Pokemon pokemon) {
        if (pokemons.contains(pokemon.getSpecies().getName())) {
            String element = this.element.getName().toLowerCase(Locale.ROOT);
            Effect.getEffect("mega_showdown:arceus_" + element).revertEffects(pokemon, List.of("multitype=normal"), null);
            if (!tradable) {
                pokemon.setTradeable(true);
            }
        }
    }

    public ElementalType getElement() {
        return element;
    }
}
