package com.github.yajatkaul.mega_showdown.render.layerEntities;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.VaryingModelRepository;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.render.layerEntities.states.DmaxHatState;
import com.github.yajatkaul.mega_showdown.codec.teraHat.LayerCodec;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.render.LayerDataLoader;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class TeraHatsLayer extends LayerEntity {
    private final ResourceLocation poserId = ResourceLocation.fromNamespaceAndPath("cobblemon", "tera_hat");
    public final DmaxHatState state = new DmaxHatState();
    private final Set<String> aspects = new HashSet<>();

    @Override
    public void render(String aspect, RenderContext context, PokemonClientDelegate clientDelegate, PokemonEntity entity, Pokemon pokemon, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (pokemon.getSpecies().getName().equals("Terapagos")) return;

        if (!Minecraft.getInstance().isPaused()) {
            long now = System.nanoTime();

            if (lastTimeNs != -1L) {
                double deltaSeconds = (now - lastTimeNs) / 1_000_000_000.0;
                animSeconds += deltaSeconds;
            }

            lastTimeNs = now;
        } else {
            lastTimeNs = System.nanoTime();
        }

        float ticks = (float) (animSeconds * 20f);

        int age = (int) ticks;
        float pt = ticks - age;

        state.updateAge(age);
        state.updatePartialTicks(pt);

        Map<String, MatrixWrapper> locatorStates = clientDelegate.getLocatorStates();
        MatrixWrapper headLocator = locatorStates.get("head");
        if (headLocator == null) return;

        poseStack.pushPose();

        LayerCodec teraHatCodec = LayerDataLoader.REGISTRY.get(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, pokemon.getSpecies().getName().toLowerCase(Locale.ROOT)));

        poseStack.mulPose(headLocator.getMatrix());
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0.08, 0.0, 0.0);

        if (teraHatCodec != null) {
            List<Float> scale = LayerCodec.getScaleForHat(pokemon, aspect, teraHatCodec);
            poseStack.scale(scale.get(0), scale.get(1), scale.get(2));
        }

        // Update state BEFORE getting model
        aspects.clear(); // Clear and re-add
        aspects.add(aspect);
        state.setCurrentAspects(aspects);

        // Get model and texture
        PosableModel model = VaryingModelRepository.INSTANCE.getPoser(poserId, state);
        ResourceLocation texture = VaryingModelRepository.INSTANCE.getTexture(poserId, state);

        model.context = context;
        model.setBufferProvider(buffer);
        state.setCurrentModel(model);

        // Setup context
        context.put(RenderContext.Companion.getASPECTS(), aspects);
        context.put(RenderContext.Companion.getTEXTURE(), texture);
        context.put(RenderContext.Companion.getSPECIES(), poserId);
        context.put(RenderContext.Companion.getPOSABLE_STATE(), state);

        // Apply animations
        model.applyAnimations(
                null,
                state,
                0F,
                0F,
                ticks,
                0F,
                0F
        );

        // Render
        VertexConsumer vertexConsumer;
        if (MegaShowdownConfig.legacyTeraEffect) {
            vertexConsumer = buffer.getBuffer(RenderType.entityCutout(texture));
        } else {
            vertexConsumer = buffer.getBuffer(MSDRenderTypes.pokemonShader(texture));
        }

        model.render(context, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -0x1);

        model.withLayerContext(
                buffer,
                state,
                VaryingModelRepository.INSTANCE.getLayers(poserId, state),
                () -> {
                    model.render(context, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -0x1);
                    return Unit.INSTANCE;
                }
        );
        model.setDefault();

        poseStack.popPose();
    }
}
