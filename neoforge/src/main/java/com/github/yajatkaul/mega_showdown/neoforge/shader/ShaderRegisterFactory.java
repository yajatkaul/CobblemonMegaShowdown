package com.github.yajatkaul.mega_showdown.neoforge.shader;

import com.github.yajatkaul.mega_showdown.render.ShaderRegister;
import com.github.yajatkaul.mega_showdown.render.renderTypes.IrisIgnoreShader;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Consumer;

public class ShaderRegisterFactory implements ShaderRegister.Factory {
    @Override
    public void register(String modid, Consumer<ShaderRegister> consumer) {
        IEventBus bus = ModList.get().getModContainerById(modid)
                .map(ModContainer::getEventBus).orElseThrow();
        Consumer<RegisterShadersEvent> cons = event -> {
            ShaderRegister register = new ShaderRegister() {
                @Override
                public ShaderInstance create(ResourceLocation location, VertexFormat format, boolean irisIgnore) throws IOException {
                    return irisIgnore ? new IrisIgnoreShader(event.getResourceProvider(), location, format) : new ShaderInstance(event.getResourceProvider(), location, format);
                }

                @Override
                public void register(ShaderInstance shaderInstance, Consumer<ShaderInstance> loadCallback) {
                    event.registerShader(shaderInstance, loadCallback);
                }
            };
            consumer.accept(register);
        };
        bus.addListener(cons);
    }
}