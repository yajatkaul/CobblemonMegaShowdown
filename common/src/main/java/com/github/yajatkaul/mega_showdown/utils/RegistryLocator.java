package com.github.yajatkaul.mega_showdown.utils;

import com.github.yajatkaul.mega_showdown.api.codec.*;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RegistryLocator {
    public static final String DU_FUSION = "du_fusion";
    public static final String SOLO_FUSION = "solo_fusion";
    public static final String FORM_CHANGE_TOGGLE_INTERACT = "form_change_toggle_interact";
    public static final String FORM_CHANGE_INTERACT = "form_change_interact";
    public static final String HELD_FORM_CHANGE = "held_form_change";
    public static final String MEGA = "mega";
    public static final String SHOWDOWN_ITEM = "showdown_item";
    public static final String Z_CRYSTAL_ITEM = "z_crystal_item";

    public static <T> T getComponent(Class<T> type, ItemStack stack) {
        ResourceLocation resourceLocation = stack.get(MegaShowdownDataComponents.RESOURCE_LOCATION_COMPONENT.get());
        String regType = stack.getOrDefault(MegaShowdownDataComponents.REGISTRY_TYPE_COMPONENT.get(), "null");

        return switch (regType) {
            case DU_FUSION -> type == DuFusion.class ?
                    type.cast(MegaShowdownDatapackRegister.DU_FUSION_REGISTRY.get(resourceLocation)) : null;
            case SOLO_FUSION -> type == SoloFusion.class ?
                    type.cast(MegaShowdownDatapackRegister.SOLO_FUSION_REGISTRY.get(resourceLocation)) : null;
            case FORM_CHANGE_TOGGLE_INTERACT -> type == FormChangeToggleInteractItem.class ?
                    type.cast(MegaShowdownDatapackRegister.FORM_CHANGE_TOGGLE_INTERACT_REGISTRY.get(resourceLocation)) : null;
            case FORM_CHANGE_INTERACT -> type == FormChangeInteractItem.class ?
                    type.cast(MegaShowdownDatapackRegister.FORM_CHANGE_INTERACT_REGISTRY.get(resourceLocation)) : null;
            case HELD_FORM_CHANGE -> type == HeldItemFormChange.class ?
                    type.cast(MegaShowdownDatapackRegister.HELD_ITEM_FORM_CHANGE_REGISTRY.get(resourceLocation)) : null;
            case MEGA -> type == MegaGimmick.class ?
                    type.cast(MegaShowdownDatapackRegister.MEGA_REGISTRY.get(resourceLocation)) : null;
            case SHOWDOWN_ITEM -> type == ShowdownItem.class ?
                    type.cast(MegaShowdownDatapackRegister.SHOWDOWN_ITEM_REGISTRY.get(resourceLocation)) : null;
            case Z_CRYSTAL_ITEM -> type == ZCrystal.class ?
                    type.cast(MegaShowdownDatapackRegister.Z_CRYSTAL_ITEM_REGISTRY.get(resourceLocation)) : null;
            default -> null;
        };
    }
}
