package com.github.yajatkaul.mega_showdown.api.codec.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ItemRenderingCodec(
        ResourceLocation itemId,
        ResourceLocation itemId_3d,
        PerspectivesCodec perspectivesCodec
) {
    public static final Codec<ItemRenderingCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("itemId").forGetter(ItemRenderingCodec::itemId),
            ResourceLocation.CODEC.fieldOf("itemId_3d").forGetter(ItemRenderingCodec::itemId_3d),
            PerspectivesCodec.CODEC.fieldOf("perspectives").forGetter(ItemRenderingCodec::perspectivesCodec)
    ).apply(instance, (itemId, itemId_3d, prespectivesCodec) -> {
        prespectivesCodec = prespectivesCodec.withDefaults(itemId);
        return new ItemRenderingCodec(itemId, itemId_3d, prespectivesCodec);
    }));
}
