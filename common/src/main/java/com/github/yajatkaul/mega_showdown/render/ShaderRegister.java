package com.github.yajatkaul.mega_showdown.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.function.Consumer;

public interface ShaderRegister {
    /**
     * Returns an instance that is safe to call from common code where you can register shader instances
     */
    Factory INSTANCE = LoaderInitializer.getImplInstance(Factory.class,
            "com.github.yajatkaul.mega_showdown.fabric.shader.ShaderRegisterFactory",
            "com.github.yajatkaul.mega_showdown.neoforge.shader.ShaderRegisterFactory");

    default ShaderInstance create(ResourceLocation location, VertexFormat format) throws IOException {
        return this.create(location, format, true);
    }

    /**
     * Create a {@link ShaderInstance} given the params
     *
     * @param irisIgnore If true returns a special instance that makes the shader render even with an active iris shader
     *                   Otherwise iris disables custom shaders.
     *                   Default is true as modded shaders generally should be ignored by iris
     */
    ShaderInstance create(ResourceLocation location, VertexFormat format, boolean irisIgnore) throws IOException;

    default void register(ResourceLocation location, VertexFormat format, Consumer<ShaderInstance> loadCallback) throws IOException {
        this.register(this.create(location, format), loadCallback);
    }

    void register(ShaderInstance shaderInstance, Consumer<ShaderInstance> loadCallback);

    interface Factory {
        void register(String modid, Consumer<ShaderRegister> consumer);
    }
}