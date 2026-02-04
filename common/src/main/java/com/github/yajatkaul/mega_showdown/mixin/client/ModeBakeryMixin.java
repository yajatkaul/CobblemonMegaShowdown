package com.github.yajatkaul.mega_showdown.mixin.client;

import com.github.yajatkaul.mega_showdown.api.codec.item.ItemRenderingCodec;
import com.github.yajatkaul.mega_showdown.render.ItemRenderingLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public abstract class ModeBakeryMixin {
    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelResourceLocation);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 1))
    private void onInit(CallbackInfo ci) {
        for (ItemRenderingCodec itemRender : ItemRenderingLoader.REGISTRY.values()) {
            this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(itemRender.itemId()));
            this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(itemRender.itemId_3d()));
        }
    }
}
