package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.particles.MinecraftParticle;
import com.github.yajatkaul.mega_showdown.api.codec.particles.SnowStormParticle;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Effect(
        Optional<MinecraftParticle> minecraft,
        Optional<SnowStormParticle> snowStorm,
        Optional<Float> battle_pause_apply,
        Optional<Float> battle_pause_revert
) {
    public static final Codec<Effect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MinecraftParticle.CODEC.optionalFieldOf("minecraft").forGetter(Effect::minecraft),
            SnowStormParticle.CODEC.optionalFieldOf("snowstorm").forGetter(Effect::snowStorm),
            Codec.FLOAT.optionalFieldOf("battle_pause_apply").forGetter(Effect::battle_pause_apply),
            Codec.FLOAT.optionalFieldOf("battle_pause_revert").forGetter(Effect::battle_pause_revert)
    ).apply(instance, Effect::new));

    public static Effect empty() {
        return new Effect(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static Effect getEffect(String effectId) {
        if (effectId == null) {
            return Effect.empty();
        }
        return Objects.requireNonNullElseGet(MegaShowdownDatapackRegister.EFFECT_REGISTRY.get(ResourceLocation.tryParse(effectId)), Effect::empty);
    }

    public static Effect getEffect(ResourceLocation effectId) {
        if (effectId == null) {
            return Effect.empty();
        }
        return Objects.requireNonNullElseGet(MegaShowdownDatapackRegister.EFFECT_REGISTRY.get(effectId), Effect::empty);
    }

    public void applyEffects(Pokemon context, List<String> aspects, @Nullable PokemonEntity other) {
        if (context.getEntity() == null) {
            AspectUtils.applyAspects(context, aspects);
            return;
        }
        if (this.snowStorm().isPresent() && this.minecraft().isPresent()) {
            this.minecraft.get().apply(context.getEntity());
            this.snowStorm.get().apply(context.getEntity(), aspects, other);
        } else if (this.minecraft().isPresent()) {
            this.minecraft.get().apply(context.getEntity());
            AspectUtils.applyAspects(context, aspects);
        } else if (this.snowStorm().isPresent()) {
            this.snowStorm.get().apply(context.getEntity(), aspects, other);
        } else {
            AspectUtils.applyAspects(context, aspects);
        }
    }

    public void revertEffects(Pokemon context, List<String> aspects, @Nullable PokemonEntity other) {
        if (context.getEntity() == null) {
            AspectUtils.applyAspects(context, aspects);
            return;
        }
        if (this.snowStorm().isPresent() && this.minecraft().isPresent()) {
            this.minecraft.get().revert(context.getEntity());
            this.snowStorm.get().revert(context.getEntity(), aspects, other);
        } else if (this.minecraft().isPresent()) {
            this.minecraft.get().revert(context.getEntity());
            AspectUtils.applyAspects(context, aspects);
        } else if (this.snowStorm().isPresent()) {
            this.snowStorm.get().revert(context.getEntity(), aspects, other);
        } else {
            AspectUtils.applyAspects(context, aspects);
        }
    }

    public void applyEffectsBattle(Pokemon context, List<String> aspects, @Nullable PokemonEntity other, BattlePokemon battlePokemon) {
        if (context.getEntity() == null) {
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
            return;
        }
        if (this.snowStorm().isPresent() && this.minecraft().isPresent()) {
            this.minecraft.get().apply(context.getEntity());
            this.snowStorm.get().applyBattle(context.getEntity(), aspects, other, battlePokemon, battle_pause_apply.orElse(1f), false);
        } else if (this.minecraft().isPresent()) {
            this.minecraft.get().apply(context.getEntity());
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
        } else if (this.snowStorm().isPresent()) {
            this.snowStorm.get().applyBattle(context.getEntity(), aspects, other, battlePokemon, battle_pause_apply.orElse(1f), false);
        } else {
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
        }
    }

    public void revertEffectsBattle(Pokemon context, List<String> aspects, @Nullable PokemonEntity other, BattlePokemon battlePokemon) {
        if (context.getEntity() == null) {
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
            return;
        }
        if (this.snowStorm().isPresent() && this.minecraft().isPresent()) {
            this.minecraft.get().revert(context.getEntity());
            this.snowStorm.get().revertBattle(context.getEntity(), aspects, other, battlePokemon, battle_pause_revert.orElse(1f));
        } else if (this.minecraft().isPresent()) {
            this.minecraft.get().revert(context.getEntity());
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
        } else if (this.snowStorm().isPresent()) {
            this.snowStorm.get().revertBattle(context.getEntity(), aspects, other, battlePokemon, battle_pause_revert.orElse(1f));
        } else {
            AspectUtils.applyAspects(context, aspects);
            AspectUtils.updatePackets(battlePokemon);
        }
    }
}
