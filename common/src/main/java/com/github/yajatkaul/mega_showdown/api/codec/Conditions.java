package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Conditions(
        Forms form,
        Aspects aspect,
        Abilities ability,
        Moves moves,
        Friendship friendship,
        List<String> aspects
) {
    public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Forms.CODEC.optionalFieldOf("form", Forms.DEFAULT()).forGetter(Conditions::form),
            Aspects.CODEC.optionalFieldOf("aspect", Aspects.DEFAULT()).forGetter(Conditions::aspect),
            Abilities.CODEC.optionalFieldOf("ability", Abilities.DEFAULT()).forGetter(Conditions::ability),
            Moves.CODEC.optionalFieldOf("move", Moves.DEFAULT()).forGetter(Conditions::moves),
            Friendship.CODEC.optionalFieldOf("friendship", Friendship.DEFAULT()).forGetter(Conditions::friendship),
            Codec.list(Codec.STRING).optionalFieldOf("aspects", List.of()).forGetter(Conditions::aspects)
    ).apply(instance, Conditions::new));

    public record Forms(
            List<String> required_forms,
            List<String> blacklist_forms
    ) {
        public static final Codec<Forms> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.STRING).optionalFieldOf("required_forms", List.of()).forGetter(Forms::required_forms),
                Codec.list(Codec.STRING).optionalFieldOf("blacklist_forms", List.of()).forGetter(Forms::blacklist_forms)
        ).apply(instance, Forms::new));

        public boolean validate(Pokemon pokemon) {
            if (!blacklist_forms.isEmpty() && blacklist_forms.contains(pokemon.getForm().getName()))
                return false;
            return required_forms.isEmpty() || required_forms.contains(pokemon.getForm().getName());
        }

        public static Forms DEFAULT() {
            return new Forms(
                    List.of(),
                    List.of()
            );
        }
    }

    public record Friendship(
            Integer min_friendship,
            Integer max_friendship
    ) {
        public static final Codec<Friendship> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("min_friendship", 0).forGetter(Friendship::min_friendship),
                Codec.INT.optionalFieldOf("max_friendship", 255).forGetter(Friendship::max_friendship)
        ).apply(instance, Friendship::new));

        public boolean validate(Pokemon pokemon) {
            return pokemon.getFriendship() >= min_friendship && pokemon.getFriendship() <= max_friendship;
        }

        public static Friendship DEFAULT() {
            return new Friendship(
                    0,
                    255
            );
        }
    }

    public record Abilities(
            List<String> required_abilities,
            List<String> blacklist_abilities
    ) {
        public static final Codec<Abilities> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.STRING).optionalFieldOf("required_abilities", List.of()).forGetter(Abilities::required_abilities),
                Codec.list(Codec.STRING).optionalFieldOf("blacklist_abilities", List.of()).forGetter(Abilities::blacklist_abilities)
        ).apply(instance, Abilities::new));

        public boolean validate(Pokemon pokemon) {
            if (!blacklist_abilities.isEmpty() &&
                    blacklist_abilities.contains(pokemon.getAbility().getName())) return false;
            return required_abilities.isEmpty() || required_abilities.contains(pokemon.getAbility().getName());
        }

        public static Abilities DEFAULT() {
            return new Abilities(
                    List.of(),
                    List.of()
            );
        }
    }

    public record Aspects(
            List<List<String>> required_aspects,
            List<List<String>> blacklist_aspects
    ) {
        public static final Codec<Aspects> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("required_aspects", List.of()).forGetter(Aspects::required_aspects),
                Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("blacklist_aspects", List.of()).forGetter(Aspects::blacklist_aspects)
        ).apply(instance, Aspects::new));

        public boolean validate(Pokemon pokemon) {
            if (!blacklist_aspects.isEmpty() && blacklist_aspects.stream().anyMatch(group -> pokemon.getAspects().containsAll(group)))
                return false;
            return required_aspects.isEmpty() || required_aspects.stream().anyMatch(group -> pokemon.getAspects().containsAll(group));
        }

        public static Aspects DEFAULT() {
            return new Aspects(
                    List.of(),
                    List.of()
            );
        }
    }

    public record Moves(
            List<List<String>> required_moves,
            List<List<String>> blacklist_moves
    ) {
        public static final Codec<Moves> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("required_moves", List.of()).forGetter(Moves::required_moves),
                Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("blacklist_moves", List.of()).forGetter(Moves::blacklist_moves)
        ).apply(instance, Moves::new));

        public boolean validate(Pokemon pokemon) {
            if (blacklist_moves.isEmpty() && required_moves.isEmpty()) return true;
            Set<String> moveNames = pokemon.getMoveSet()
                    .getMoves()
                    .stream()
                    .map(Move::getName)
                    .collect(Collectors.toSet());

            if (!blacklist_moves.isEmpty()
                    && blacklist_moves.stream().anyMatch(moveNames::containsAll))
                return false;

            if (!required_moves.isEmpty())
                return required_moves.stream().anyMatch(moveNames::containsAll);

            return true;
        }

        public static Moves DEFAULT() {
            return new Moves(
                    List.of(),
                    List.of()
            );
        }
    }

    public boolean validate(Pokemon pokemon) {
        if (!form.validate(pokemon)) return false;
        if (!aspect.validate(pokemon)) return false;
        if (!moves.validate(pokemon)) return false;
        if (!friendship.validate(pokemon)) return false;

        return ability.validate(pokemon);
    }

    public static Conditions DEFAULT() {
        return new Conditions(
                Forms.DEFAULT(),
                Aspects.DEFAULT(),
                Abilities.DEFAULT(),
                Moves.DEFAULT(),
                Friendship.DEFAULT(),
                List.of()
        );
    }
}
