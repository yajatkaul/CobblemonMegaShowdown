package com.github.yajatkaul.mega_showdown.neoforge.mixin;

import com.cobblemon.mod.common.data.CobblemonDataProvider;
import com.github.yajatkaul.mega_showdown.neoforge.datapack.CustomTypeRegistry;
import com.github.yajatkaul.mega_showdown.neoforge.datapack.showdown.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CobblemonDataProvider.class, remap = false)
public class CobblemonDataProviderMixin {

    @Inject(method = "registerDefaults", at = @At(value = "HEAD"))
    private void register(CallbackInfo ci) {
        CobblemonDataProvider self = (CobblemonDataProvider) (Object) this;

        self.register(CustomTypeRegistry.INSTANCE, true);
        self.register(Moves.INSTANCE, true);
        self.register(Abilities.INSTANCE, true);
        self.register(Conditions.INSTANCE, true);
        self.register(HeldItems.INSTANCE, true);
        self.register(TypeCharts.INSTANCE, true);
        self.register(Scripts.INSTANCE, true);
    }
}