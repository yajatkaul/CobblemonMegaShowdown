package com.github.yajatkaul.mega_showdown.api.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ZCrystal(
        String showdown_item_id,
        String color
) {
    public static final Codec<ZCrystal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("showdown_item_id").forGetter(ZCrystal::showdown_item_id),
            Codec.STRING.fieldOf("color").forGetter(ZCrystal::color)
    ).apply(instance, ZCrystal::new));
}