package com.github.yajatkaul.mega_showdown.fabric;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.MegaShowdownClient;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlockEntities;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlocks;
import com.github.yajatkaul.mega_showdown.block.block_entity.renderer.PedestalBlockEntityRenderer;
import com.github.yajatkaul.mega_showdown.render.ItemRenderingLoader;
import com.github.yajatkaul.mega_showdown.render.RegisterShaderEvent;
import com.github.yajatkaul.mega_showdown.render.renderTypes.IrisIgnoreShader;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.github.yajatkaul.mega_showdown.screen.MegaShowdownMenuTypes;
import com.github.yajatkaul.mega_showdown.screen.custom.screen.TeraPouchScreen;
import com.github.yajatkaul.mega_showdown.screen.custom.screen.ZygardeCubeScreen;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public final class MegaShowdownFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new ItemRenderingLoader());
        MenuScreens.register(MegaShowdownMenuTypes.ZYGARDE_CUBE_MENU.get(), ZygardeCubeScreen::new);
        MenuScreens.register(MegaShowdownMenuTypes.TERA_POUCH_MENU.get(), TeraPouchScreen::new);

        MegaShowdownClient.init();

        BlockRenderLayerMap.INSTANCE.putBlock(MegaShowdownBlocks.GRACIDEA_FLOWER.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MegaShowdownBlocks.POTTED_GRACIDEA.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MegaShowdownBlocks.REASSEMBLY_UNIT.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MegaShowdownBlocks.WISHING_STAR_CRYSTAL.get(), RenderType.translucent());

        ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "gyaradosjumpingmega"),
                FabricLoader.getInstance().getModContainer(MegaShowdown.MOD_ID).orElseThrow(),
                Component.translatable("message.mega_showdown.gyrados_jump_mega"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "regionbiasmsd"),
                FabricLoader.getInstance().getModContainer(MegaShowdown.MOD_ID).orElseThrow(),
                Component.translatable("message.mega_showdown.region_bias_msd"),
                ResourcePackActivationType.NORMAL
        );

        BlockEntityRenderers.register(MegaShowdownBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntityRenderer::new);

        RegisterShaderEvent.EVENT.register((event) -> {
            MSDRenderTypes.teraShader = event.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_shader"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );
        });
    }
}
