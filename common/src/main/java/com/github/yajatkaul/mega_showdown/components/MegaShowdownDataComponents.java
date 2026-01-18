package com.github.yajatkaul.mega_showdown.components;

import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class MegaShowdownDataComponents {
    private static final DeferredRegister<DataComponentType<?>> REGISTRAR =
            DeferredRegister.create(MegaShowdown.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final Supplier<DataComponentType<PokemonStorge>> POKEMON_STORAGE = REGISTRAR.register(
            "pokemon_storage",
            () -> DataComponentType.<PokemonStorge>builder()
                    .persistent(PokemonStorge.CODEC)
                    .build()
    );

    public static final Supplier<DataComponentType<TeraType>> TERA_TYPE = REGISTRAR.register(
            "tera_type",
            () -> DataComponentType.<TeraType>builder()
                    .persistent(TeraType.getBY_IDENTIFIER_CODEC())
                    .build()
    );

    public static final Supplier<DataComponentType<InventoryStorage>> INVENTORY = REGISTRAR.register(
            "inventory",
            () -> DataComponentType.<InventoryStorage>builder()
                    .persistent(InventoryStorage.CODEC)
                    .build()
    );

    public static final Supplier<DataComponentType<Integer>> LIKO_PENDANT_TICK_COMPONENT = REGISTRAR.register(
            "liko_pendant_tick_component",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .build()
    );

    public static final Supplier<DataComponentType<String>> REGISTRY_TYPE_COMPONENT = REGISTRAR.register(
            "registry_type_component",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .build()
    );

    public static final Supplier<DataComponentType<ResourceLocation>> RESOURCE_LOCATION_COMPONENT = REGISTRAR.register(
            "registry_location_component",
            () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .build()
    );

    public static void register() {
        REGISTRAR.register();
    }
}
