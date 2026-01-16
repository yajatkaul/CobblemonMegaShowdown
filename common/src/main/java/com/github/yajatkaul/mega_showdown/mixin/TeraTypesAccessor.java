package com.github.yajatkaul.mega_showdown.mixin;

import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(value = TeraTypes.class, remap = false)
public interface TeraTypesAccessor {
    @Accessor("types")
    HashMap<ResourceLocation, TeraType> getTypes();
}
