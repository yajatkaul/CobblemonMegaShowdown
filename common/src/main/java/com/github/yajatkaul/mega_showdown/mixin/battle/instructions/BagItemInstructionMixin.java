package com.github.yajatkaul.mega_showdown.mixin.battle.instructions;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.interpreter.instructions.BagItemInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(value = BagItemInstruction.class, remap = false)
public class BagItemInstructionMixin {
    @Final
    @Shadow
    private BattleMessage message;

    @Inject(method = "invoke", at = @At("TAIL"))
    private void invokeInject(PokemonBattle battle, CallbackInfo ci) {
        BattlePokemon battlePokemon = message.battlePokemon(0, battle);
        Pokemon pokemon = battlePokemon.getEffectedPokemon();
        String item = message.argumentAt(1);

        if (pokemon != null) {
            String bagitem_name = "mega_showdown:bagitem_" + item;
            Effect.getEffect(bagitem_name).applyEffectsBattle(pokemon, List.of(), Optional.empty(), null, battlePokemon);
        }
    }
}
