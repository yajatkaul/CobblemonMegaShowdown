package com.github.yajatkaul.mega_showdown.fabric.mixin;

import com.github.yajatkaul.mega_showdown.render.renderTypes.IrisIgnoreShader;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShaderInstance.class)
public abstract class ShaderInstanceMixin {

    @Shadow
    @Final
    private String name;

    @SuppressWarnings("UnstableApiUsage")
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), allow = 1)
    private ResourceLocation modifyId(String id, Operation<ResourceLocation> original) {
        if ((Object) this instanceof IrisIgnoreShader) {
            return FabricShaderProgram.rewriteAsId(id, this.name);
        }

        return original.call(id);
    }
}