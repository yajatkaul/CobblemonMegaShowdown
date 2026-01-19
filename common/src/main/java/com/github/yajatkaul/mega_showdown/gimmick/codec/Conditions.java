package com.github.yajatkaul.mega_showdown.gimmick.codec;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Conditions(
        List<String> required_forms,
        List<String> blacklist_forms,
        List<List<String>> required_aspects,
        List<List<String>> blacklist_aspects,
        List<String> aspects
) {
    public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).optionalFieldOf("required_forms", List.of()).forGetter(Conditions::required_forms),
            Codec.list(Codec.STRING).optionalFieldOf("blacklist_forms", List.of()).forGetter(Conditions::blacklist_forms),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("required_aspects", List.of()).forGetter(Conditions::required_aspects),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("blacklist_aspects", List.of()).forGetter(Conditions::blacklist_aspects),
            Codec.list(Codec.STRING).optionalFieldOf("aspects", List.of()).forGetter(Conditions::aspects)
    ).apply(instance, Conditions::new));

    public boolean validate(Pokemon pokemon) {
        if (!blacklist_aspects.isEmpty() && blacklist_aspects.stream().anyMatch(group -> pokemon.getAspects().containsAll(group)))
            return false;
        if (!blacklist_forms.isEmpty() && blacklist_forms.contains(pokemon.getForm().getName()))
            return false;
        if (!required_forms.isEmpty() && !required_forms.contains(pokemon.getForm().getName()))
            return false;
        return required_aspects.isEmpty() || required_aspects.stream().anyMatch(group -> pokemon.getAspects().containsAll(group));
    }

    public static Conditions DEFAULT() {
        return new Conditions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }
}
