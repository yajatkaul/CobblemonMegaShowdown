package com.github.yajatkaul.mega_showdown.render.layerEntities;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.VaryingModelRepository;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.sizer.LayerCodec;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.render.LayerDataLoader;
import com.github.yajatkaul.mega_showdown.render.layerEntities.states.TeraHatState;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import kotlin.Unit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeraHatsLayer extends LayerEntity {
    private final ResourceLocation poserId = ResourceLocation.fromNamespaceAndPath("cobblemon", "tera_hat");

    public TeraHatsLayer() {
        super(new TeraHatState());
    }

    @Override
    public void render(String aspect, RenderContext context, PokemonClientDelegate clientDelegate, PokemonEntity entity, Pokemon pokemon, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (pokemon.getSpecies().getName().equals("Terapagos")) return;

        super.render(context, clientDelegate, entity, pokemon, entityYaw, poseStack, buffer, packedLight);
        state.setCurrentAspects(Set.of(aspect));

        Map<String, MatrixWrapper> locatorStates = clientDelegate.getLocatorStates();
        MatrixWrapper headLocator = locatorStates.get("head");
        if (headLocator == null) return;

        // Get model and texture
        PosableModel model = VaryingModelRepository.INSTANCE.getPoser(poserId, state);
        ResourceLocation texture = VaryingModelRepository.INSTANCE.getTexture(poserId, state);

        model.context = context;
        model.setBufferProvider(buffer);
        state.setCurrentModel(model);

        // Setup context
        context.put(RenderContext.Companion.getASPECTS(), Set.of(aspect));
        context.put(RenderContext.Companion.getTEXTURE(), texture);
        context.put(RenderContext.Companion.getSPECIES(), poserId);
        context.put(RenderContext.Companion.getPOSABLE_STATE(), state);

        LayerCodec.Settings settings = LayerDataLoader.getSettings(pokemon, aspect);

        poseStack.pushPose();

        poseStack.mulPose(headLocator.getMatrix());
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        if (settings != null) {
            List<Float> translate = settings.translate();
            poseStack.translate(translate.get(0), translate.get(1), translate.get(2));
        }

        if (settings != null) {
            List<Float> scale = settings.scale();
            poseStack.scale(scale.get(0), scale.get(1), scale.get(2));
        }

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
            vertexConsumer = buffer.getBuffer(MSDRenderTypes.pokemonShader(texture, aspect));
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
