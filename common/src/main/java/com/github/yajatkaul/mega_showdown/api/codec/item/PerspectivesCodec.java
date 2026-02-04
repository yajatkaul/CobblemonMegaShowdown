package com.github.yajatkaul.mega_showdown.api.codec.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record PerspectivesCodec(
        Optional<ResourceLocation> headLoc,
        Optional<ResourceLocation> guiLoc,
        Optional<ResourceLocation> groundLoc,
        Optional<ResourceLocation> fixedLoc,
        Optional<ResourceLocation> hand,
        Optional<ResourceLocation> hand3rd
) {
    public static final Codec<PerspectivesCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("headLoc").forGetter(PerspectivesCodec::headLoc),
            ResourceLocation.CODEC.optionalFieldOf("guiLoc").forGetter(PerspectivesCodec::guiLoc),
            ResourceLocation.CODEC.optionalFieldOf("groundLoc").forGetter(PerspectivesCodec::groundLoc),
            ResourceLocation.CODEC.optionalFieldOf("fixedLoc").forGetter(PerspectivesCodec::fixedLoc),
            ResourceLocation.CODEC.optionalFieldOf("hand").forGetter(PerspectivesCodec::hand),
            ResourceLocation.CODEC.optionalFieldOf("hand3rd").forGetter(PerspectivesCodec::hand3rd)
    ).apply(instance, PerspectivesCodec::new));

    public PerspectivesCodec withDefaults(ResourceLocation defaultId) {
        return new PerspectivesCodec(
                headLoc.or(() -> Optional.of(defaultId)),
                guiLoc.or(() -> Optional.of(defaultId)),
                groundLoc.or(() -> Optional.of(defaultId)),
                fixedLoc.or(() -> Optional.of(defaultId)),
                hand.or(() -> Optional.of(defaultId)),
                hand3rd.or(() -> Optional.of(defaultId))
        );
    }
}
