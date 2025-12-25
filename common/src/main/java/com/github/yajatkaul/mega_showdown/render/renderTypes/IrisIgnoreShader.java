package com.github.yajatkaul.mega_showdown.render.renderTypes;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class IrisIgnoreShader extends ShaderInstance {
    public IrisIgnoreShader(ResourceProvider provider, ResourceLocation location, VertexFormat format) throws IOException {
        super(provider, location.toString(), format);
    }

    public boolean iris$shouldSkipThis() {
        return false;
    }
}