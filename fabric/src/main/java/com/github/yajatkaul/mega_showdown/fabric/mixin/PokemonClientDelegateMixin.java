package com.github.yajatkaul.mega_showdown.fabric.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.fabric.utils.KotlinHelperFabric;
import com.github.yajatkaul.mega_showdown.utils.TeraHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = PokemonClientDelegate.class, remap = false)
public class PokemonClientDelegateMixin {
    @Shadow
    public PokemonEntity currentEntity;
    @Unique
    private long mega_showdown$lastTeraParticle;
    @Unique
    private float mega_showdown$secondsSinceLastTeraParticle() {
        return (System.currentTimeMillis() - mega_showdown$lastTeraParticle) / 1000F;
    }
    @Unique
    private final float mega_showdown$teraParticleCooldown = 2.0F;

    @Inject(method = "tick(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;)V", at = @At(value = "TAIL"))
    private void tick(PokemonEntity entity, CallbackInfo ci) {
        mega_showdown$playTera();
    }

    @Unique
    private void mega_showdown$playTera() {
        PokemonClientDelegate self = (PokemonClientDelegate) (Object) this;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        PokemonEntity entity = currentEntity;

        Optional<String> aspect = entity.getAspects().stream()
                .filter(a -> a.startsWith("msd:tera_")).findFirst();
        if (aspect.isEmpty()) return;

        double distance = player.position().distanceTo(entity.position());
        if (distance > Cobblemon.config.getShinyNoticeParticlesDistance()) return;

        if (mega_showdown$secondsSinceLastTeraParticle() > mega_showdown$teraParticleCooldown) {
            KotlinHelperFabric.INSTANCE.playParticleEffect(TeraHelper.getTeraAnimationFromAspect(aspect.get()), "root", self.getRuntime());
            mega_showdown$lastTeraParticle = System.currentTimeMillis();
        }
    }
}
