package com.github.yajatkaul.mega_showdown.datapack;

import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.*;
import com.github.yajatkaul.mega_showdown.gimmick.MaxGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class MegaShowdownDatapackRegister {
    public static final ResourceKey<Registry<Effect>> EFFECT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "effect"));

    public static final ResourceKey<Registry<FormChangeToggleInteractItem>> FORM_CHANGE_TOGGLE_INTERACT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "form_change_toggle_interact"));
    public static final ResourceKey<Registry<FormChangeInteractItem>> FORM_CHANGE_INTERACT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "form_change_interact"));
    public static final ResourceKey<Registry<SoloFusion>> SOLO_FUSION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "solo_fusion"));
    public static final ResourceKey<Registry<DuFusion>> DU_FUSION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "du_fusion"));
    public static final ResourceKey<Registry<MaxGimmick>> GMAX_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "gmax"));
    public static final ResourceKey<Registry<HeldItemFormChange>> HELD_ITEM_FORM_CHANGE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "held_form_change"));
    public static final ResourceKey<Registry<MegaGimmick>> MEGA_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "mega"));
    public static final ResourceKey<Registry<ShowdownItem>> SHOWDOWN_ITEM_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "showdown_item"));
    public static final ResourceKey<Registry<ZCrystal>> Z_CRYSTAL_ITEM_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "z_crystal_item"));
    public static final ResourceKey<Registry<BattleFormChange>> BATTLE_FORM_CHANGE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "battle_form"));
    public static final ResourceKey<Registry<BattleEffect>> BATTLE_EFFECT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "battle_effect"));

    public static Registry<MegaGimmick> MEGA_REGISTRY;
    public static Registry<ShowdownItem> SHOWDOWN_ITEM_REGISTRY;
    public static Registry<MaxGimmick> GMAX_REGISTRY;
    public static Registry<SoloFusion> SOLO_FUSION_REGISTRY;
    public static Registry<DuFusion> DU_FUSION_REGISTRY;
    public static Registry<HeldItemFormChange> HELD_ITEM_FORM_CHANGE_REGISTRY;
    public static Registry<FormChangeToggleInteractItem> FORM_CHANGE_TOGGLE_INTERACT_REGISTRY;
    public static Registry<BattleFormChange> BATTLE_FORM_CHANGE_REGISTRY;
    public static Registry<Effect> EFFECT_REGISTRY;
    public static Registry<FormChangeInteractItem> FORM_CHANGE_INTERACT_REGISTRY;
    public static Registry<ZCrystal> Z_CRYSTAL_ITEM_REGISTRY;
    public static Registry<BattleEffect> BATTLE_EFFECT_REGISTRY;

    public static void registerShowdownDatapackItems(MinecraftServer server) {
        EFFECT_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.EFFECT_REGISTRY_KEY);
        MEGA_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.MEGA_REGISTRY_KEY);
        SHOWDOWN_ITEM_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.SHOWDOWN_ITEM_REGISTRY_KEY);
        GMAX_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.GMAX_REGISTRY_KEY);
        SOLO_FUSION_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.SOLO_FUSION_REGISTRY_KEY);
        DU_FUSION_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.DU_FUSION_REGISTRY_KEY);
        HELD_ITEM_FORM_CHANGE_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.HELD_ITEM_FORM_CHANGE_REGISTRY_KEY);
        FORM_CHANGE_TOGGLE_INTERACT_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.FORM_CHANGE_TOGGLE_INTERACT_REGISTRY_KEY);
        BATTLE_FORM_CHANGE_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.BATTLE_FORM_CHANGE_REGISTRY_KEY);
        FORM_CHANGE_INTERACT_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.FORM_CHANGE_INTERACT_REGISTRY_KEY);
        Z_CRYSTAL_ITEM_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.Z_CRYSTAL_ITEM_REGISTRY_KEY);
        BATTLE_EFFECT_REGISTRY = server.registryAccess().registryOrThrow(MegaShowdownDatapackRegister.BATTLE_EFFECT_REGISTRY_KEY);

        CobblemonHeldItemManager.INSTANCE.registerStackRemap(stack -> {
            ShowdownItem showdownItem = RegistryLocator.getComponent(ShowdownItem.class, stack);
            if (showdownItem != null) {
                return showdownItem.showdown_item_id();
            }

            MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, stack);
            if (megaGimmick != null) {
                return megaGimmick.showdown_id();
            }

            ZCrystal zCrystal = RegistryLocator.getComponent(ZCrystal.class, stack);
            if (zCrystal != null) {
                return zCrystal.showdown_item_id();
            }

            return null;
        });
    }
}
