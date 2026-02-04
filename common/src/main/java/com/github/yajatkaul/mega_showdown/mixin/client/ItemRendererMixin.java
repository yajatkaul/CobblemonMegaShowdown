package com.github.yajatkaul.mega_showdown.mixin.client;

import com.github.yajatkaul.mega_showdown.api.codec.item.ItemRenderingCodec;
import com.github.yajatkaul.mega_showdown.api.codec.item.PerspectivesCodec;
import com.github.yajatkaul.mega_showdown.render.ItemRenderingLoader;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Shadow
    public abstract ItemModelShaper getItemModelShaper();

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    public BakedModel modifyModel(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ItemDisplayContext renderMode) {
        ItemRenderingCodec itemRenderingCodec = ItemRenderingLoader.REGISTRY.getOrDefault(BuiltInRegistries.ITEM.getKey(stack.getItem()), null);

        if (itemRenderingCodec != null) {
            PerspectivesCodec perspectives = itemRenderingCodec.perspectivesCodec();

            if (renderMode == ItemDisplayContext.GUI) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.guiLoc().get()));
            } else if (renderMode == ItemDisplayContext.HEAD) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.headLoc().get()));
            } else if (renderMode == ItemDisplayContext.GROUND) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.groundLoc().get()));
            } else if (renderMode == ItemDisplayContext.FIXED) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.fixedLoc().get()));
            } else if (renderMode == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.hand().get()));
            } else if (renderMode == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.hand().get()));
            } else if (renderMode == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.hand3rd().get()));
            } else if (renderMode == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(perspectives.hand3rd().get()));
            }
        }
        return bakedModel;
    }

    @ModifyVariable(
            method = "getModel",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    public BakedModel getHeldItemModelMixin(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack) {
        ItemRenderingCodec itemRenderingCodec = ItemRenderingLoader.REGISTRY.getOrDefault(BuiltInRegistries.ITEM.getKey(stack.getItem()), null);

        if (itemRenderingCodec != null) {
            return this.itemModelShaper.getModelManager().getModel(ModelResourceLocation.inventory(itemRenderingCodec.itemId_3d()));
        }
        return bakedModel;
    }
}
