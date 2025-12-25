package com.github.yajatkaul.mega_showdown.fabric.mixin;

import com.github.yajatkaul.mega_showdown.render.RegisterShaderEvent;
import com.github.yajatkaul.mega_showdown.render.ShaderRegister;
import com.github.yajatkaul.mega_showdown.render.renderTypes.IrisIgnoreShader;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "reloadShaders", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;loadBlurEffect(Lnet/minecraft/server/packs/resources/ResourceProvider;)V"))
    private void onShaderLoad(ResourceProvider provider, CallbackInfo info, @Local(ordinal = 1) List<Pair<ShaderInstance, Consumer<ShaderInstance>>> programs) throws IOException {
        ShaderRegister register = new ShaderRegister() {

            @SuppressWarnings("UnstableApiUsage")
            @Override
            public ShaderInstance create(ResourceLocation location, VertexFormat format, boolean irisIgnore) throws IOException {
                return irisIgnore ? new IrisIgnoreShader(provider, location, format) : new FabricShaderProgram(provider, location, format);
            }

            @Override
            public void register(ShaderInstance shaderInstance, Consumer<ShaderInstance> loadCallback) {
                programs.add(Pair.of(shaderInstance, loadCallback));
            }
        };
        RegisterShaderEvent.EVENT.invoker().registerShaders(register);
    }
}