package com.github.yajatkaul.mega_showdown.api.codec.particles;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import com.github.yajatkaul.mega_showdown.utils.PokemonBehaviourHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kotlin.Unit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record SnowStormParticle(
        Optional<List<String>> source_apply,
        Optional<List<String>> target_apply,
        Optional<List<String>> source_revert,
        Optional<List<String>> target_revert,
        Optional<String> particle_apply,
        Optional<Float> apply_after,
        Optional<String> particle_revert,
        Optional<Float> revert_after,
        Optional<SoundCodec> sound_apply,
        Optional<SoundCodec> sound_revert,
        Optional<AnimationData> animations
) {
    public static final Codec<SnowStormParticle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).optionalFieldOf("source_apply").forGetter(SnowStormParticle::source_apply),
            Codec.list(Codec.STRING).optionalFieldOf("target_apply").forGetter(SnowStormParticle::target_apply),
            Codec.list(Codec.STRING).optionalFieldOf("source_revert").forGetter(SnowStormParticle::source_revert),
            Codec.list(Codec.STRING).optionalFieldOf("target_revert").forGetter(SnowStormParticle::target_revert),
            Codec.STRING.optionalFieldOf("particle_apply").forGetter(SnowStormParticle::particle_apply),
            Codec.FLOAT.optionalFieldOf("apply_after").forGetter(SnowStormParticle::apply_after),
            Codec.STRING.optionalFieldOf("particle_revert").forGetter(SnowStormParticle::particle_revert),
            Codec.FLOAT.optionalFieldOf("revert_after").forGetter(SnowStormParticle::revert_after),
            SoundCodec.CODEC.optionalFieldOf("sound_apply").forGetter(SnowStormParticle::sound_apply),
            SoundCodec.CODEC.optionalFieldOf("sound_revert").forGetter(SnowStormParticle::sound_revert),
            AnimationData.CODEC.optionalFieldOf("animations").forGetter(SnowStormParticle::animations)
    ).apply(instance, SnowStormParticle::new));

    public void apply(PokemonEntity context, List<String> aspects, PokemonEntity other) {
        processTransformation(context, aspects, other, null, true);
    }

    public void revert(PokemonEntity context, List<String> aspects, PokemonEntity other) {
        processTransformation(context, aspects, other, null, false);
    }

    public void applyBattle(PokemonEntity context, List<String> aspects, PokemonEntity other, BattlePokemon battlePokemon, float battle_pause, boolean loop) {
        battlePokemon.actor.getBattle().dispatchWaitingToFront(battle_pause, () -> Unit.INSTANCE);
        processTransformation(context, aspects, other, battlePokemon, true);
    }

    public void revertBattle(PokemonEntity context, List<String> aspects, PokemonEntity other, BattlePokemon battlePokemon, float battle_pause) {
        battlePokemon.actor.getBattle().dispatchWaitingToFront(battle_pause, () -> Unit.INSTANCE);
        processTransformation(context, aspects, other, battlePokemon, false);
    }

    private void processTransformation(PokemonEntity context, List<String> aspects, PokemonEntity other,
                                       BattlePokemon battlePokemon, boolean isApply) {
        context.setNoAi(true);

        CompoundTag pokemonPersistentData = context.getPokemon().getPersistentData();
        pokemonPersistentData.putBoolean("form_changing", true);

        AspectUtils.appendRevertDataPokemon(
                Effect.empty(),
                aspects,
                context.getPokemon(),
                "aspects"
        );

        Optional<String> particle = isApply ? particle_apply : particle_revert;
        Optional<List<String>> sourceParticles = isApply ? source_apply : source_revert;
        Optional<List<String>> targetParticles = isApply ? target_apply : target_revert;
        Optional<Float> delay = isApply ? apply_after : revert_after;

        if (particle.isEmpty()) {
            applyAspectsAndCleanup(context, aspects, pokemonPersistentData, battlePokemon);
            return;
        }

        animations.ifPresent(animation -> {
            if (isApply) {
                animation.applyAnimations(context);
            } else {
                animation.revertAnimations(context);
            }
        });

        ResourceLocation particleId = ResourceLocation.tryParse(particle.get());
        if (particleId == null) {
            MegaShowdown.LOGGER.error("Invalid snowstorm {} particle{}",
                    isApply ? "apply" : "revert",
                    battlePokemon != null ? " during battle" : "");
            return;
        }

        PokemonBehaviourHelper.Companion.snowStormPartileSpawner(
                context, particleId, sourceParticles.orElse(null), other, targetParticles.orElse(null)
        );
        playSound(context, isApply);

        delay.ifPresentOrElse(
                delayValue -> context.after(delayValue, () -> {
                    applyAspectsAndCleanup(context, aspects, pokemonPersistentData, battlePokemon);
                    return Unit.INSTANCE;
                }),
                () -> applyAspectsAndCleanup(context, aspects, pokemonPersistentData, battlePokemon)
        );
    }

    private void applyAspectsAndCleanup(PokemonEntity context, List<String> aspects,
                                        CompoundTag pokemonPersistentData, BattlePokemon battlePokemon) {
        AspectUtils.applyAspects(context.getPokemon(), aspects);

        if (battlePokemon != null) {
            AspectUtils.updatePackets(battlePokemon);
        }

        resetFormChangingState(context, pokemonPersistentData);
    }

    private void resetFormChangingState(PokemonEntity context, CompoundTag pokemonPersistentData) {
        context.setNoAi(false);

        pokemonPersistentData.remove("form_changing");
        pokemonPersistentData.remove("aspects");
    }

    private void playSound(PokemonEntity context, boolean isApply) {
        Optional<SoundCodec> sound = isApply ? sound_apply : sound_revert;

        sound.ifPresent((soundCodec -> soundCodec.play(context)));
    }
}