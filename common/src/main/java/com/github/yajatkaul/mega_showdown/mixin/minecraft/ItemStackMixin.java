package com.github.yajatkaul.mega_showdown.mixin.minecraft;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.api.codec.DuFusion;
import com.github.yajatkaul.mega_showdown.api.codec.FormChangeInteractItem;
import com.github.yajatkaul.mega_showdown.api.codec.FormChangeToggleInteractItem;
import com.github.yajatkaul.mega_showdown.api.codec.SoloFusion;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "onDestroyed", at = @At("HEAD"))
    private void onDestroyed(ItemEntity itemEntity, CallbackInfo ci) {
        if (!itemEntity.level().isClientSide) {
            ItemStack stack = itemEntity.getItem();

            DuFusion duFusion = RegistryLocator.getComponent(DuFusion.class, stack);
            if (duFusion != null) {
                duFusion.onDestroyed(itemEntity);
            }
            SoloFusion soloFusion = RegistryLocator.getComponent(SoloFusion.class, stack);
            if (soloFusion != null) {
                soloFusion.onDestroyed(itemEntity);
            }
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useOnEntity(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            DuFusion duFusion = RegistryLocator.getComponent(DuFusion.class, stack);
            if (duFusion != null) {
                cir.setReturnValue(duFusion.use(level, player, interactionHand));
            }
            SoloFusion soloFusion = RegistryLocator.getComponent(SoloFusion.class, stack);
            if (soloFusion != null) {
                cir.setReturnValue(soloFusion.use(level, player, interactionHand));
            }
        }
    }

    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void useOnEntity(Player player, LivingEntity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.level().isClientSide) {
            if (entity instanceof PokemonEntity pokemonEntity) {
                ItemStack stack = player.getItemInHand(interactionHand);
                FormChangeToggleInteractItem formChangeToggleInteractItem = RegistryLocator.getComponent(FormChangeToggleInteractItem.class, stack);

                if (formChangeToggleInteractItem != null) {
                    cir.setReturnValue(formChangeToggleInteractItem.interactLivingEntity(player, pokemonEntity, stack));
                }

                FormChangeInteractItem formChangeInteractItem = RegistryLocator.getComponent(FormChangeInteractItem.class, stack);

                if (formChangeInteractItem != null) {
                    cir.setReturnValue(formChangeInteractItem.interactLivingEntity(player, pokemonEntity, stack));
                }
            }
        }
    }
}
