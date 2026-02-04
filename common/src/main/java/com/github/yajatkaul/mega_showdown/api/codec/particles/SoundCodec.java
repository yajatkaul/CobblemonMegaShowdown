package com.github.yajatkaul.mega_showdown.api.codec.particles;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public record SoundCodec(
        String id,
        float vol,
        float pitch
) {
    public static final Codec<SoundCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("sound_id").forGetter(SoundCodec::id),
            Codec.FLOAT.optionalFieldOf("vol", 1f).forGetter(SoundCodec::vol),
            Codec.FLOAT.optionalFieldOf("pitch", 1f).forGetter(SoundCodec::pitch)
    ).apply(instance, SoundCodec::new));

    public void play(PokemonEntity context) {
        SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.tryParse(id));
        Vec3 entityPos = context.position();

        if (soundEvent == null) {
            MegaShowdown.LOGGER.error("Invalid Sound, sound id: {}", id);
        } else {
            context.level().playSound(
                    null, entityPos.x, entityPos.y, entityPos.z,
                    soundEvent,
                    SoundSource.PLAYERS, vol, pitch
            );
        }
    }
}
