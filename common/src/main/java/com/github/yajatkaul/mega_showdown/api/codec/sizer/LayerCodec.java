package com.github.yajatkaul.mega_showdown.api.codec.sizer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record LayerCodec(
        String pokemon,
        Map<String, Map<String, Settings>> size_config
) {
    public static final Codec<LayerCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pokemon").forGetter(LayerCodec::pokemon),
            Codec.unboundedMap(Codec.STRING, Codec.unboundedMap(Codec.STRING, Settings.CODEC)).fieldOf("size_config").forGetter(LayerCodec::size_config)
    ).apply(instance, LayerCodec::new));

    public record Settings(
            List<Float> scale,
            List<Float> translate
    ) {
        public static final Codec<Settings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.FLOAT).optionalFieldOf("scale", List.of(1f, 1f, 1f)).forGetter(Settings::scale),
                Codec.list(Codec.FLOAT).optionalFieldOf("translate", List.of(0f, 0f, 0f)).forGetter(Settings::translate)
        ).apply(instance, Settings::new));
    }
}
