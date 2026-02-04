package com.github.yajatkaul.mega_showdown.render;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.sizer.LayerCodec;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LayerDataLoader implements ResourceManagerReloadListener {
    private static final String DIRECTORY = "msd_sizer";
    public static final HashMap<String, SizerRegStruct> LAYER_REGISTRY = new HashMap<>();

    public static class SizerRegStruct {
        private final HashMap<String, HashMap<String, LayerCodec.Settings>> settingsHashMap = new HashMap<>();

        public void addForm(String formName, String aspect, LayerCodec.Settings settings) {
            HashMap<String, LayerCodec.Settings> map = settingsHashMap.getOrDefault(formName, new HashMap<>());
            map.put(aspect, settings);
            settingsHashMap.putIfAbsent(formName, map);
        }

        public LayerCodec.Settings getSettings(String formName, String aspect) {
            return settingsHashMap.getOrDefault(formName, new HashMap<>()).get(aspect);
        }
    }

    public static LayerCodec.Settings getSettings(Pokemon pokemon, String aspect) {
        String name = pokemon.getSpecies().resourceIdentifier.getPath();
        String formName = pokemon.getForm().getName();

        SizerRegStruct reg = LAYER_REGISTRY.get(name);
        if (reg == null) return null;

        return reg.getSettings(formName, aspect);
    }

    public static void load() {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        LAYER_REGISTRY.clear();

        Collection<ResourceLocation> resources =
                rm.listResources(DIRECTORY, path -> path.getPath().endsWith(".json")).keySet();

        for (ResourceLocation id : resources) {
            try (var stream = rm.getResource(id).get().open()) {
                LayerCodec codec = LayerCodec.CODEC.parse(
                        JsonOps.INSTANCE,
                        JsonParser.parseReader(new InputStreamReader(stream))
                ).result().orElseThrow();

                LAYER_REGISTRY.putIfAbsent(codec.pokemon(), new SizerRegStruct());
                SizerRegStruct sizerRegStruct = LAYER_REGISTRY.get(codec.pokemon());

                for (String form : codec.size_config().keySet()) {
                    for (Map.Entry<String, LayerCodec.Settings> aspectSetting : codec.size_config().get(form).entrySet()) {
                        sizerRegStruct.addForm(form, aspectSetting.getKey(), aspectSetting.getValue());
                    }
                }
            } catch (Exception e) {
                MegaShowdown.LOGGER.error("Failed loading layer JSON: {}", id, e);
            }
        }

        MegaShowdown.LOGGER.info("Loaded {} custom layer data", LAYER_REGISTRY.size());
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
        load();
        return ResourceManagerReloadListener.super.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public @NotNull String getName() {
        return "mega_showdown";
    }
}