package com.github.yajatkaul.mega_showdown.datapack;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.data.JsonDataRegistry;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import com.cobblemon.mod.common.api.types.tera.elemental.ElementalTypeTeraType;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.mixin.TeraTypesAccessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomTypeRegistry implements JsonDataRegistry<CustomTypeRegistry.CustomTypeData> {

    public static final CustomTypeRegistry INSTANCE = new CustomTypeRegistry();

    public final List<ElementalType> customTypes = new ArrayList<>();
    private final SimpleObservable<CustomTypeRegistry> observable = new SimpleObservable<>();

    private CustomTypeRegistry() {}

    @Override
    public @NotNull ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Cobblemon.MODID, "mega_showdown/custom_type");
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
    public @NotNull TypeToken<CustomTypeData> getTypeToken() {
        return TypeToken.get(CustomTypeData.class);
    }

    @Override
    public @NotNull String getResourcePath() {
        return "mega_showdown/custom_type";
    }

    @Override
    public @NotNull SimpleObservable<CustomTypeRegistry> getObservable() {
        return observable;
    }

    @Override
    public void sync(@NotNull ServerPlayer player) {
        // Custom types are synced as part of ElementalTypes, so no additional sync needed
    }

    @Override
    public void reload(@NotNull Map<ResourceLocation, ? extends CustomTypeData> data) {
        customTypes.clear();

        data.forEach((identifier, typeData) -> {
            try {
                ElementalType newType = ElementalTypes.register(new ElementalType(
                        typeData.name,
                        Component.translatable(typeData.text),
                        typeData.hue,
                        0,
                        ResourceLocation.fromNamespaceAndPath(Cobblemon.MODID, "ui/types.png"),
                        typeData.id
                ));

                MegaShowdown.LOGGER.info("Added one");
                customTypes.add(newType);

                // Create and register TeraType
                TeraType newTeraType = new ElementalTypeTeraType(newType);
                ((TeraTypesAccessor) (Object) TeraTypes.INSTANCE)
                        .getTypes()
                        .put(MiscUtilsKt.cobblemonResource(typeData.id), newTeraType);

//                Cobblemon.LOGGER.info("Loaded custom type: {} ({})", identifier, typeData.name);
            } catch (Exception e) {
                Cobblemon.LOGGER.error("Error loading custom type {}: {}", identifier, e.getMessage());
            }
        });

        Cobblemon.LOGGER.info("Loaded {} custom elemental types", customTypes.size());
        observable.emit(this);
    }

    public static class CustomTypeData {
        public String name;
        public String id;
        public int hue;
        public String text;

        // Gson requires a no-arg constructor
        public CustomTypeData() {}

        public CustomTypeData(String name, String id, int hue, String text) {
            this.name = name;
            this.id = id;
            this.hue = hue;
            this.text = text;
        }
    }
}