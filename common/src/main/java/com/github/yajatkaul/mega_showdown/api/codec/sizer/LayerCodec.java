package com.github.yajatkaul.mega_showdown.api.codec.sizer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record LayerCodec(
        String pokemon,
        Map<String, Map<String, Settings>> size_config
) {
    public static final Codec<LayerCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pokemon").forGetter(LayerCodec::pokemon),
            Codec.unboundedMap(Codec.STRING, Codec.unboundedMap(Codec.STRING, Settings.CODEC)).fieldOf("size_config").forGetter(LayerCodec::size_config)
    ).apply(instance, LayerCodec::new));

    public record Settings(
            Optional<List<Float>> scale,
            Optional<List<Float>> translate,
            Optional<List<Float>> rotation
    ) {
        public static final Codec<Settings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(Codec.FLOAT).optionalFieldOf("scale").forGetter(Settings::scale),
                Codec.list(Codec.FLOAT).optionalFieldOf("translate").forGetter(Settings::translate),
                Codec.list(Codec.FLOAT).optionalFieldOf("rotation").forGetter(Settings::rotation)
        ).apply(instance, Settings::new));
    }
}
