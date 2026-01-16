package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.data.JsonDataRegistry;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TeraGimmick implements JsonDataRegistry<TeraGimmick.TeraData> {
    public static final TeraGimmick INSTANCE = new TeraGimmick();

    public final HashMap<String, String> aspectColorMap = new HashMap<>();
    public final HashMap<String, ShaderInstance> teraShaderMap = new HashMap<>();
    private final SimpleObservable<TeraGimmick> observable = new SimpleObservable<>();

    private TeraGimmick() {}

    @Override
    public @NotNull ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "mega_showdown/tera");
    }

    @Override
    public @NotNull PackType getType() {
        return PackType.SERVER_DATA;
    }

    @Override
    public @NotNull Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    @Override
    public @NotNull TypeToken<TeraData> getTypeToken() {
        return TypeToken.get(TeraData.class);
    }

    @Override
    public @NotNull String getResourcePath() {
        return "mega_showdown/tera";
    }

    @Override
    public @NotNull SimpleObservable<TeraGimmick> getObservable() {
        return observable;
    }

    @Override
    public void sync(@NotNull ServerPlayer player) {
        // Custom types are synced as part of ElementalTypes, so no additional sync needed
    }

    @Override
    public void reload(@NotNull Map<ResourceLocation, ? extends TeraData> data) {
        teraShaderMap.clear();

        data.forEach((identifier, typeData) -> {
            try {
                for (Map.Entry<String, String> entry : typeData.aspectShaderMap.entrySet()) {
                    teraShaderMap.put(
                            entry.getKey(),
                            getColorShaderMap().get(entry.getValue())
                    );
                    aspectColorMap.put(
                            entry.getKey(),
                            entry.getValue()
                    );
                }
            } catch (Exception e) {
                Cobblemon.LOGGER.error("Error loading tera data {}: {}", identifier, e.getMessage());
            }
        });

        Cobblemon.LOGGER.info("Loaded {} tera data", teraShaderMap.size());
        observable.emit(this);
    }

    public static class TeraData {
        public Map<String, String> aspectShaderMap;

        public TeraData(Map<String, String> typeShaderMap) {
            this.aspectShaderMap = typeShaderMap;
        }
    }

    private static final Map<String, ShaderInstance> COLOR_SHADER_MAP = new HashMap<>();

    private static Map<String, ShaderInstance> getColorShaderMap() {
        if (COLOR_SHADER_MAP.isEmpty()) {
            COLOR_SHADER_MAP.put("red", MSDRenderTypes.teraFire);
            COLOR_SHADER_MAP.put("blue", MSDRenderTypes.teraWater);
            COLOR_SHADER_MAP.put("green", MSDRenderTypes.teraGrass);
            COLOR_SHADER_MAP.put("yellow", MSDRenderTypes.teraElectric);
            COLOR_SHADER_MAP.put("brown", MSDRenderTypes.teraGround);
            COLOR_SHADER_MAP.put("light_blue", MSDRenderTypes.teraFlying);
            COLOR_SHADER_MAP.put("purple", MSDRenderTypes.teraDragon);
            COLOR_SHADER_MAP.put("pink", MSDRenderTypes.teraFairy);
            COLOR_SHADER_MAP.put("black", MSDRenderTypes.teraDark);
            COLOR_SHADER_MAP.put("gray", MSDRenderTypes.teraSteel);
            COLOR_SHADER_MAP.put("light_grey", MSDRenderTypes.teraIce);
            COLOR_SHADER_MAP.put("orange", MSDRenderTypes.teraFighting);
            COLOR_SHADER_MAP.put("lime", MSDRenderTypes.teraBug);
            COLOR_SHADER_MAP.put("teal", MSDRenderTypes.teraPoison);
            COLOR_SHADER_MAP.put("indigo", MSDRenderTypes.teraGhost);
            COLOR_SHADER_MAP.put("magenta", MSDRenderTypes.teraPsychic);
            COLOR_SHADER_MAP.put("tan", MSDRenderTypes.teraRock);
            COLOR_SHADER_MAP.put("navy", MSDRenderTypes.teraNormal);
            COLOR_SHADER_MAP.put("white", MSDRenderTypes.teraStellar);
        }
        return COLOR_SHADER_MAP;
    }
}