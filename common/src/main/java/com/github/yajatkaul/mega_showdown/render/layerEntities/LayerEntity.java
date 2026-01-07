package com.github.yajatkaul.mega_showdown.render.layerEntities;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class LayerEntity {
    protected double animSeconds = 0.0;
    protected long lastTimeNs = -1L;

    public void render(RenderContext context, PokemonClientDelegate clientDelegate, PokemonEntity entity, Pokemon pokemon, float entityYaw, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {}

    public void render(String aspect, RenderContext context, PokemonClientDelegate clientDelegate, PokemonEntity entity, Pokemon pokemon, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {}
}
