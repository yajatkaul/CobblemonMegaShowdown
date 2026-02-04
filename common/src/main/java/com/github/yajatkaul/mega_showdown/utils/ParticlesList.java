package com.github.yajatkaul.mega_showdown.utils;

import com.github.yajatkaul.mega_showdown.api.codec.particles.AnimationData;
import com.github.yajatkaul.mega_showdown.api.codec.particles.MinecraftParticle;
import com.github.yajatkaul.mega_showdown.api.codec.particles.SoundCodec;

import java.util.List;
import java.util.Optional;

public class ParticlesList {
    public static MinecraftParticle calyrexDynamaxLevelUpParticles = simpleMinecraftParticlesApply(
            "minecraft:soul_fire_flame",
            new SoundCodec("minecraft:block.amethyst_block.chime", 1, 1),
            0.5f
    );

    public static MinecraftParticle otherDynamaxLevelUpParticles = simpleMinecraftParticlesApply(
            "minecraft:flame",
            new SoundCodec("minecraft:block.amethyst_block.chime", 1, 1),
            0.5f
    );

    public static MinecraftParticle glowParticles = simpleMinecraftParticlesApply(
            "minecraft:glow",
            new SoundCodec("minecraft:block.amethyst_block.chime", 1, 1),
            1.2f
    );

    public static MinecraftParticle simpleMinecraftParticlesApply(String id_apply, SoundCodec sound_apply, float scale) {
        return new MinecraftParticle(
                Optional.of(id_apply),
                Optional.empty(),
                Optional.of(sound_apply),
                Optional.empty(),
                Optional.of(scale),
                Optional.empty(),
                Optional.of(new AnimationData(List.of("cry"), List.of(), List.of(), List.of(), 0, 0))
        );
    }
}
