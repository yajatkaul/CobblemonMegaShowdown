package com.github.yajatkaul.mega_showdown.api.codec.particles;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record MinecraftParticle(
        Optional<String> particle_apply,
        Optional<String> particle_revert,
        Optional<SoundCodec> sound_apply,
        Optional<SoundCodec> sound_revert,
        Optional<Float> particle_apply_amplifier,
        Optional<Float> particle_revert_amplifier,
        Optional<AnimationData> animations
) {
    public static final Codec<MinecraftParticle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("particle_apply").forGetter(MinecraftParticle::particle_apply),
            Codec.STRING.optionalFieldOf("particle_revert").forGetter(MinecraftParticle::particle_revert),
            SoundCodec.CODEC.optionalFieldOf("sound_apply").forGetter(MinecraftParticle::sound_apply),
            SoundCodec.CODEC.optionalFieldOf("sound_revert").forGetter(MinecraftParticle::sound_revert),
            Codec.FLOAT.optionalFieldOf("particle_apply_amplifier").forGetter(MinecraftParticle::particle_apply_amplifier),
            Codec.FLOAT.optionalFieldOf("particle_revert_amplifier").forGetter(MinecraftParticle::particle_revert_amplifier),
            AnimationData.CODEC.optionalFieldOf("animations").forGetter(MinecraftParticle::animations)
    ).apply(instance, MinecraftParticle::new));

    public void apply(PokemonEntity context) {
        if (context == null) {
            return;
        }
        if (context.level() instanceof ServerLevel serverLevel) {
            Vec3 entityPos = context.position();

            this.animations.ifPresent((animation) -> {
                animation.applyAnimations(context);
            });

            this.sound_apply.ifPresent((sound_apply) -> {
                sound_apply.play(context);
            });

            this.particle_apply.ifPresent((particle_apply) -> {
                String[] partsParticle;
                partsParticle = particle_apply.split(":");

                ResourceLocation custom_particle_id = ResourceLocation.fromNamespaceAndPath(partsParticle[0], partsParticle[1]);
                ParticleType<?> particleType = BuiltInRegistries.PARTICLE_TYPE.get(custom_particle_id);

                double entityWidth = context.getBbWidth();
                double entityHeight = context.getBbHeight();

                int baseParticleCount = (int) (100 * entityWidth * entityHeight);
                float amplifier = 1;

                if (this.particle_apply_amplifier.isPresent()) {
                    amplifier = Math.max(1, this.particle_apply_amplifier.get());
                }

                float particleCount = baseParticleCount * amplifier; // Amplify particle count
                double radius = entityWidth * (0.8 + amplifier * 0.1); // Slightly increase radius with amplifier

                if (particleType instanceof ParticleOptions particle) {
                    for (int i = 0; i < particleCount; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double xOffset = Math.cos(angle) * radius;
                        double zOffset = Math.sin(angle) * radius;
                        double yOffset = Math.random() * entityHeight;

                        serverLevel.sendParticles(
                                particle,
                                entityPos.x + xOffset,
                                entityPos.y + yOffset,
                                entityPos.z + zOffset,
                                1,
                                0, 0, 0,
                                0.1
                        );
                    }
                } else {
                    MegaShowdown.LOGGER.error("Invalid Particle Apply used for pokemons: {}, particle id: {}",
                            context.getPokemon().getSpecies().getName(),
                            particle_apply);
                }
            });
        }
    }

    public void revert(PokemonEntity context) {
        if (context == null) {
            return;
        }
        if (context.level() instanceof ServerLevel serverLevel) {
            Vec3 entityPos = context.position();

            this.animations.ifPresent((animation) -> {
                animation.revertAnimations(context);
            });

            this.sound_revert.ifPresent((sound_revert) -> {
                sound_revert.play(context);
            });

            this.particle_revert.ifPresent((particle_revert) -> {
                String[] partsParticle;
                partsParticle = particle_revert.split(":");

                ResourceLocation custom_particle_id = ResourceLocation.fromNamespaceAndPath(partsParticle[0], partsParticle[1]);
                ParticleType<?> particleType = BuiltInRegistries.PARTICLE_TYPE.get(custom_particle_id);

                double entityWidth = context.getBbWidth();
                double entityHeight = context.getBbHeight();

                int baseParticleCount = (int) (100 * entityWidth * entityHeight);
                float amplifier = 1;

                if (this.particle_revert_amplifier.isPresent()) {
                    amplifier = Math.max(1, this.particle_revert_amplifier.get());
                }

                float particleCount = baseParticleCount * amplifier; // Amplify particle count
                double radius = entityWidth * (0.8 + amplifier * 0.1); // Slightly increase radius with amplifier

                if (particleType instanceof ParticleOptions particle) {
                    for (int i = 0; i < particleCount; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double xOffset = Math.cos(angle) * radius;
                        double zOffset = Math.sin(angle) * radius;
                        double yOffset = Math.random() * entityHeight;

                        serverLevel.sendParticles(
                                particle,
                                entityPos.x + xOffset,
                                entityPos.y + yOffset,
                                entityPos.z + zOffset,
                                1,
                                0, 0, 0,
                                0.1
                        );
                    }
                } else {
                    MegaShowdown.LOGGER.error("Invalid Particle Revert used for pokemons: {}, particle id: {}",
                            context.getPokemon().getSpecies().getName(),
                            particle_revert);
                }
            });
        }
    }
}