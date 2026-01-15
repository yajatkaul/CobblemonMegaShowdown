package com.github.yajatkaul.mega_showdown.codec.sizer;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.render.LayerDataLoader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public record LayerCodec(
        Map<String, List<Float>> size_config,
        Map<String, Map<String, List<Float>>> forms
) {
    private static final List<Float> DEFAULT_SCALE = List.of(1f, 1f, 1f);

    public static final Codec<LayerCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.list(Codec.FLOAT)).fieldOf("size_config").forGetter(LayerCodec::size_config),
            Codec.unboundedMap(Codec.STRING, Codec.unboundedMap(Codec.STRING, Codec.list(Codec.FLOAT))).fieldOf("forms").forGetter(LayerCodec::forms)
    ).apply(instance, LayerCodec::new));

    public static List<Float> getScaleForHat(Pokemon pokemon, String tera_hat, LayerCodec teraHatCodec) {
        Map<String, List<Float>> hatConfigs = teraHatCodec.forms.get(pokemon.getForm().getName());
        if (hatConfigs != null) {
            return hatConfigs.getOrDefault(tera_hat, DEFAULT_SCALE);
        }
        return teraHatCodec.size_config.getOrDefault(tera_hat, DEFAULT_SCALE);
    }

    public static LayerCodec getLayerCodec(String name) {
        name = name.toLowerCase().replace(" ", "");
        return LayerDataLoader.REGISTRY.get(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "msd_sizer/" + name + ".json"));
    }
}
