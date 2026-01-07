package com.github.yajatkaul.mega_showdown.mixin.client;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.bedrock.animation.BedrockActiveAnimation;
import com.cobblemon.mod.common.client.render.models.blockbench.bedrock.animation.BedrockAnimationRepository;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.VaryingModelRepository;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.codec.teraHat.LayerCodec;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.render.LayerDataLoader;
import com.github.yajatkaul.mega_showdown.render.layerEntities.DynamaxCloudsLayer;
import com.github.yajatkaul.mega_showdown.render.layerEntities.TeraHatsLayer;
import com.github.yajatkaul.mega_showdown.render.layerEntities.states.TeraCrystalState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

/*
IK this code is shit but im bored doing this so ill fix it later
 */
@Mixin(value = PokemonRenderer.class)
public class PokemonRendererMixin {
    @Unique
    private final RenderContext mega_showdown$context = new RenderContext();

    @Unique
    private DynamaxCloudsLayer mega_showdown$dynamaxCloudsLayer = new DynamaxCloudsLayer();
    @Unique
    private TeraHatsLayer mega_showdown$teraHatsLayer = new TeraHatsLayer();

    //Tera crystal
    @Unique
    private final ResourceLocation mega_showdown$teraCrystalPoserId = ResourceLocation.fromNamespaceAndPath("cobblemon", "terastal_transformation");
    @Unique
    public final TeraCrystalState mega_showdown$teraCrystalState = new TeraCrystalState();
    @Unique
    private final Set<String> mega_showdown$teraCrystalAspects = new HashSet<>();
    @Unique
    private boolean mega_showdown$teraCrystalPlayed = false;
    @Unique
    private boolean mega_showdown$teraCrystalPass = false;
    //Timershit
    @Unique
    private double mega_showdown$animCrystalSeconds = 0.0;
    @Unique
    private long mega_showdown$lastCrystalTimeNs = -1L;
    //

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.mega_showdown$context.put(RenderContext.Companion.getRENDER_STATE(), RenderContext.RenderState.WORLD);
        this.mega_showdown$context.put(RenderContext.Companion.getDO_QUIRKS(), true);
    }

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        PokemonClientDelegate clientDelegate = (PokemonClientDelegate) entity.getDelegate();

        Pokemon pokemon = entity.getPokemon();
        boolean tera_play = pokemon.getAspects().contains("play_tera");
        Optional<String> aspect = pokemon.getAspects().stream()
                .filter(a -> a.startsWith("msd:tera_")).findFirst();

        if (tera_play && (!mega_showdown$teraCrystalPlayed || mega_showdown$teraCrystalPass) && aspect.isPresent()) {
            mega_showdown$renderTeraCrystals(
                    entity,
                    pokemon,
                    clientDelegate,
                    poseStack,
                    buffer,
                    packedLight
            );
            if (!mega_showdown$teraCrystalPass) {
                return;
            }
        }

        boolean dmax_aspect = pokemon.getAspects().contains("msd:dmax");

        if (aspect.isPresent()) {
            if (MegaShowdownConfig.teraHats) {
               mega_showdown$teraHatsLayer.render(aspect.get(), mega_showdown$context, clientDelegate, entity, pokemon, entityYaw, partialTicks, poseStack, buffer, packedLight);
            }
        }

        if (dmax_aspect) {
            mega_showdown$dynamaxCloudsLayer.render(mega_showdown$context, clientDelegate, entity, pokemon, entityYaw, poseStack, buffer, packedLight);
        }
    }

    @Unique
    private void mega_showdown$renderTeraCrystals(PokemonEntity entity, Pokemon pokemon, PokemonClientDelegate clientDelegate, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!Minecraft.getInstance().isPaused()) {
            long now = System.nanoTime();

            if (mega_showdown$lastCrystalTimeNs != -1L) {
                double deltaSeconds = (now - mega_showdown$lastCrystalTimeNs) / 1_000_000_000.0;
                mega_showdown$animCrystalSeconds += deltaSeconds;
            }

            mega_showdown$lastCrystalTimeNs = now;
        } else {
            mega_showdown$lastCrystalTimeNs = System.nanoTime();
        }

        float mega_showdown$teraCrystalDuration = new BedrockActiveAnimation(
                BedrockAnimationRepository.INSTANCE.getAnimation("terastal_transformation", "animation.terastal_transformation.transform")
        ).getDuration();

        if (mega_showdown$teraCrystalState.getAnimationSeconds() >= mega_showdown$teraCrystalDuration) {
            mega_showdown$teraCrystalPlayed = true;
            mega_showdown$teraCrystalPass = false;
            mega_showdown$animCrystalSeconds = 0.0;
            mega_showdown$lastCrystalTimeNs = -1L;
            mega_showdown$teraCrystalState.resetAnimation();
            entity.after(3f, () -> {
                mega_showdown$teraCrystalPlayed = false;
                return Unit.INSTANCE;
            });
            return;
        } else if (mega_showdown$teraCrystalState.getAnimationSeconds() >= mega_showdown$teraCrystalDuration - 0.3) {
            mega_showdown$teraCrystalPass = true;
        }

        float ticks = (float) (mega_showdown$animCrystalSeconds * 20f);

        int age = (int) ticks;
        float pt = ticks - age;

        mega_showdown$teraCrystalState.updateAge(age);
        mega_showdown$teraCrystalState.updatePartialTicks(pt);

        mega_showdown$teraCrystalState.setCurrentAspects(mega_showdown$teraCrystalAspects);

        Map<String, MatrixWrapper> locatorStates = clientDelegate.getLocatorStates();
        MatrixWrapper rootLocator = locatorStates.get("root");

        if (rootLocator == null) return;

        LayerCodec crystalSize = LayerDataLoader.REGISTRY.get(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, pokemon.getSpecies().getName().toLowerCase(Locale.ROOT)));

        // Get model and texture
        PosableModel model = VaryingModelRepository.INSTANCE.getPoser(mega_showdown$teraCrystalPoserId, mega_showdown$teraCrystalState);
        model.context = mega_showdown$context;
        ResourceLocation texture = VaryingModelRepository.INSTANCE.getTexture(mega_showdown$teraCrystalPoserId, mega_showdown$teraCrystalState);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(texture));

        model.setBufferProvider(buffer);
        mega_showdown$teraCrystalState.setCurrentModel(model);

        // Setup context
        mega_showdown$context.put(RenderContext.Companion.getASPECTS(), mega_showdown$teraCrystalAspects);
        mega_showdown$context.put(RenderContext.Companion.getTEXTURE(), texture);
        mega_showdown$context.put(RenderContext.Companion.getSPECIES(), mega_showdown$teraCrystalPoserId);
        mega_showdown$context.put(RenderContext.Companion.getPOSABLE_STATE(), mega_showdown$teraCrystalState);

        poseStack.pushPose();

        poseStack.mulPose(rootLocator.getMatrix());
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0.08, 0.0, 0.0);

        poseStack.scale(1.5f, 1.5f, 1.5f);

        if (crystalSize != null) {
            List<Float> scale = LayerCodec.getScaleForHat(pokemon, "msd:tera_crystal", crystalSize);
            poseStack.scale(scale.get(0), scale.get(1), scale.get(2));
        }

        // Apply animations
        model.applyAnimations(
                null,
                mega_showdown$teraCrystalState,
                0F,
                0F,
                ticks,
                0F,
                0F

        );

        // Render
        model.render(mega_showdown$context, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -0x1);

        model.withLayerContext(
                buffer,
                mega_showdown$teraCrystalState,
                VaryingModelRepository.INSTANCE.getLayers(mega_showdown$teraCrystalPoserId, mega_showdown$teraCrystalState),
                () -> {
                    model.render(mega_showdown$context, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -0x1);
                    return Unit.INSTANCE;
                }
        );
        model.setDefault();
        poseStack.popPose();
    }
}
