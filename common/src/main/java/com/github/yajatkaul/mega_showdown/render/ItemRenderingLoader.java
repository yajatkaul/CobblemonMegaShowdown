package com.github.yajatkaul.mega_showdown.render;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.item.ItemRenderingCodec;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ItemRenderingLoader implements ResourceManagerReloadListener {
    public static final HashMap<ResourceLocation, ItemRenderingCodec> REGISTRY = new HashMap<>();
    private static final String DIRECTORY = "item_rendering";

    public static void load() {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        REGISTRY.clear();

        Collection<ResourceLocation> resources =
                rm.listResources(DIRECTORY, path -> path.getPath().endsWith(".json")).keySet();

        for (ResourceLocation id : resources) {
            try (var stream = rm.getResource(id).get().open()) {
                ItemRenderingCodec codec = ItemRenderingCodec.CODEC.parse(
                        JsonOps.INSTANCE,
                        JsonParser.parseReader(new InputStreamReader(stream))
                ).result().orElseThrow();
                REGISTRY.put(codec.itemId(), codec);
            } catch (Exception e) {
                MegaShowdown.LOGGER.error("Failed loading item_rendering JSON: {}", id, e);
            }
        }

        MegaShowdown.LOGGER.info("Loaded {} custom rendering entries", REGISTRY.size());
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