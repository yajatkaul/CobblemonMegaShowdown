package com.github.yajatkaul.mega_showdown.neoforge;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.MegaShowdownClient;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlockEntities;
import com.github.yajatkaul.mega_showdown.block.block_entity.renderer.MegaStoneStandRenderer;
import com.github.yajatkaul.mega_showdown.block.block_entity.renderer.PedestalBlockEntityRenderer;
import com.github.yajatkaul.mega_showdown.render.ItemRenderingLoader;
import com.github.yajatkaul.mega_showdown.render.LayerDataLoader;
import com.github.yajatkaul.mega_showdown.render.RegisterShaderEvent;
import com.github.yajatkaul.mega_showdown.render.TeraMapLoader;
import com.github.yajatkaul.mega_showdown.render.renderTypes.IrisIgnoreShader;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.github.yajatkaul.mega_showdown.screen.MegaShowdownMenuTypes;
import com.github.yajatkaul.mega_showdown.screen.custom.screen.TeraPouchScreen;
import com.github.yajatkaul.mega_showdown.screen.custom.screen.ZygardeCubeScreen;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = MegaShowdown.MOD_ID, value = Dist.CLIENT)
public class MegaShowdownNeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MegaShowdownClient.init();

        RegisterShaderEvent.EVENT.register((shaderEvent) -> {
            MSDRenderTypes.teraFire = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fire"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraWater = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_water"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraGrass = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_grass"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraElectric = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_electric"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraIce = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ice"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraFighting = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fighting"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraPoison = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_poison"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraGround = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ground"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraFlying = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_flying"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraPsychic = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_psychic"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraBug = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_bug"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraRock = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_rock"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraGhost = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ghost"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraDragon = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_dragon"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraDark = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_dark"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraSteel = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_steel"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraFairy = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fairy"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraNormal = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_normal"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );

            MSDRenderTypes.teraStellar = shaderEvent.create(
                    ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_stellar"),
                    DefaultVertexFormat.NEW_ENTITY,
                    true
            );
        });
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MegaShowdownMenuTypes.ZYGARDE_CUBE_MENU.get(), ZygardeCubeScreen::new);
        event.register(MegaShowdownMenuTypes.TERA_POUCH_MENU.get(), TeraPouchScreen::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MegaShowdownBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MegaShowdownBlockEntities.MEGA_STONE_STAND_BLOCK_ENTITY.get(), MegaStoneStandRenderer::new);
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new ItemRenderingLoader());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new LayerDataLoader());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new TeraMapLoader());

        if (event.getPackType() != PackType.CLIENT_RESOURCES)
            return;

        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "resourcepacks/gyaradosjumpingmega"),
                PackType.CLIENT_RESOURCES,
                Component.translatable("message.mega_showdown.gyrados_jump_mega"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "resourcepacks/regionbiasmsd"),
                PackType.CLIENT_RESOURCES,
                Component.translatable("message.mega_showdown.region_bias_msd"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fire"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraFire = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_water"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraWater = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_grass"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraGrass = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_electric"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraElectric = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ice"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraIce = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fighting"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraFighting = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_poison"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraPoison = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ground"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraGround = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_flying"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraFlying = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_psychic"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraPsychic = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_bug"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraBug = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_rock"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraRock = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_ghost"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraGhost = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_dragon"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraDragon = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_dark"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraDark = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_steel"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraSteel = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_fairy"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraFairy = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_normal"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraNormal = shaderInstance
        );

        event.registerShader(
                new IrisIgnoreShader(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "tera_crystal_stellar"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shaderInstance -> MSDRenderTypes.teraStellar = shaderInstance
        );

    }
}
