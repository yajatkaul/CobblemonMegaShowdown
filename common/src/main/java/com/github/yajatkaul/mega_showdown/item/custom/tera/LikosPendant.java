package com.github.yajatkaul.mega_showdown.item.custom.tera;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.item.custom.ToolTipItem;
import com.github.yajatkaul.mega_showdown.utils.PokemonBehaviourHelper;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.data.SlotTypeLoader;
import io.wispforest.accessories.impl.ExpandedSimpleContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LikosPendant extends ToolTipItem {
    public LikosPendant(Properties properties) {
        super(properties);
    }

    public static String ticksToTime(int ticks) {
        int minutes = (int) Math.floor(ticks / 1200.0);
        int seconds = (int) Math.floor((ticks % 1200) / 20.0);

        String formattedSeconds = String.format("%02d", seconds);

        if (minutes <= 0) {
            return formattedSeconds;
        } else {
            return minutes + ":" + formattedSeconds;
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        AccessoriesCapability capability = AccessoriesCapability.get(player);

        if (level.isClientSide || capability == null) {
            return InteractionResultHolder.pass(stack);
        }

        AccessoriesContainer slot = capability.getContainer(SlotTypeLoader.getSlotType(level, "tera_slot"));

        if (slot == null) {
            MegaShowdown.LOGGER.info("No tera_slot found");
            return InteractionResultHolder.pass(stack);
        }

        ExpandedSimpleContainer accessories = slot.getAccessories();

        for (int i = 0; i < accessories.getContainerSize(); i++) {
            if (accessories.getItem(i).isEmpty()) {
                accessories.setItem(i, stack.copy());
                player.setItemInHand(interactionHand, ItemStack.EMPTY);

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
                break;
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if (entity.level().isClientSide) return;

        int tick = stack.getOrDefault(MegaShowdownDataComponents.LIKO_PENDANT_TICK_COMPONENT.get(), MegaShowdownConfig.likoPendentDuration);

        if (tick > 0) {
            tick--;
            stack.set(MegaShowdownDataComponents.LIKO_PENDANT_TICK_COMPONENT.get(), tick);

            if (tick % 20 == 0) {
                Component loreLine = Component.literal(ticksToTime(tick))
                        .withStyle(ChatFormatting.WHITE);
                stack.set(DataComponents.LORE, new ItemLore(List.of(loreLine)));
            }
        }

        if (tick <= 0) {
            int shinyRoll = ThreadLocalRandom.current().nextInt(1, (int) (Cobblemon.config.getShinyRate() + 1)); // 8193 is exclusive

            stack.shrink(1);

            PokemonEntity terapagos = PokemonProperties.Companion.parse("terapagos").createEntity(level);
            terapagos.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 0, false, false));
            terapagos.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120, 0, false, false));

            terapagos.getPokemon().setTeraType(TeraTypes.getSTELLAR());
            if (shinyRoll == 1) {
                terapagos.getPokemon().setShiny(true);
            }

            // Get the direction the entity is facing (yaw in degrees)
            float yaw = entity.getYRot();

            // Convert yaw to radians and calculate direction vector
            double radians = Math.toRadians(yaw);
            double dx = -Math.sin(radians);
            double dz = Math.cos(radians);

            // Distance in front of entity
            double distance = 2.0;

            // Compute spawn coordinates
            double spawnX = entity.getX() + dx * distance;
            double spawnY = entity.getY();
            double spawnZ = entity.getZ() + dz * distance;

            terapagos.moveTo(spawnX, spawnY, spawnZ, entity.getYRot(), 0.0f);

            level.addFreshEntity(terapagos);

            terapagos.setNoAi(true);
            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.tryParse("mega_showdown:terapagos_spawn"));
            Vec3 entityPos = terapagos.position();
            PokemonBehaviourHelper.Companion.snowStormPartileSpawner(
                    terapagos, ResourceLocation.parse("cobblemon:pendant_effect"), List.of("target"), null, null
            );
            terapagos.level().playSound(
                    null, entityPos.x, entityPos.y, entityPos.z,
                    soundEvent,
                    SoundSource.PLAYERS, 0.6f, 1f
            );
        }
    }
}
