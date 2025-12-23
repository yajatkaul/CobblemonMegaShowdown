package com.github.yajatkaul.mega_showdown.mixin.client;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.VaryingModelRepository;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.github.yajatkaul.mega_showdown.utils.GlowHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @WrapOperation(
            method = "render*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
    )
    private VertexConsumer mega_showdown$applyPokemonShader(
            MultiBufferSource buffer,
            RenderType original,
            Operation<VertexConsumer> originalCall,
            T entity
    ) {
        if (MegaShowdownConfig.legacyTeraEffect) {
            return originalCall.call(buffer, original);
        }
        if (!(entity instanceof PokemonEntity pokemon)) {
            return originalCall.call(buffer, original);
        }

        Optional<String> aspect = pokemon.getAspects().stream()
                .filter(a -> a.startsWith("msd:tera_")).findFirst();
        if (aspect.isEmpty()) {
            return originalCall.call(buffer, original);
        }

        PokemonClientDelegate delegate =
                (PokemonClientDelegate) pokemon.getDelegate();

        ResourceLocation texture =
                VaryingModelRepository.INSTANCE.getTexture(
                        pokemon.getPokemon().getSpecies().getResourceIdentifier(),
                        delegate
                );

        MSDRenderTypes.teraShader.safeGetUniform("TeraTint").set(GlowHandler.getTeraColor(aspect.get()));
        return buffer.getBuffer(MSDRenderTypes.pokemonShader(texture));
    }
}
