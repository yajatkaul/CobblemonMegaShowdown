package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.interpreter.ContextManager;
import com.github.yajatkaul.mega_showdown.battle.effect.*;
import com.github.yajatkaul.mega_showdown.battle.messaging.BattlePacketManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = PokemonBattle.class, remap = false)
public abstract class PokemonBattleMixin {
    @Shadow
    private int ticks;

    @Shadow
    public abstract ContextManager getContextManager();

    @Shadow
    public abstract Iterable<BattleSide> getSides();

    @Inject(method = "tick", at = @At("HEAD"))
    private void applyFieldEffects(CallbackInfo info) {
        if (this.ticks % 20 != 0) return;

        Collection<BattleContext> terrain = this.getContextManager().get(BattleContext.Type.TERRAIN);
        Collection<BattleContext> weather = this.getContextManager().get(BattleContext.Type.WEATHER);
        Collection<BattleContext> room = this.getContextManager().get(BattleContext.Type.ROOM);
        if (terrain != null)
            terrain.stream().findAny().ifPresent(context -> TerrainEffect.handleTerrain((PokemonBattle) (Object) this, context.getId(), this.ticks));
        if (weather != null)
            weather.stream().findAny().ifPresent(context -> WeatherEffect.handleWeather((PokemonBattle) (Object) this, context.getId()));
        if (room != null)
            room.stream().findAny().ifPresent(context -> RoomEffect.handleRoom((PokemonBattle) (Object) this, context.getId()));

        for (BattleSide side : this.getSides()) {
            Collection<BattleContext> hazard = side.getContextManager().get(BattleContext.Type.HAZARD);
            Collection<BattleContext> screen = side.getContextManager().get(BattleContext.Type.SCREEN);
            Collection<BattleContext> tailwind = side.getContextManager().get(BattleContext.Type.TAILWIND);
            if (hazard != null) hazard.forEach(context -> HazardEffect.handleHazard(side, context.getId(), this.ticks));
            if (screen != null) screen.forEach(context -> ScreenEffect.handleScreen(side, context.getId(), this.ticks));
            if (tailwind != null)
                tailwind.stream().findAny().ifPresent(context -> TailwindEffect.handleTailwind(side, context.getId()));
        }
    }

    @Inject(method = "turn", at = @At("HEAD"))
    private void updateBattleGuiPackets (int newTurnNumber, CallbackInfo info) {
        BattlePacketManager.update((PokemonBattle)(Object)this);
    }
}
