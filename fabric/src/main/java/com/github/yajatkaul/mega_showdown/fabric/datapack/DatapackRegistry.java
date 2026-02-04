package com.github.yajatkaul.mega_showdown.fabric.datapack;

import com.github.yajatkaul.mega_showdown.api.codec.*;
import com.github.yajatkaul.mega_showdown.gimmick.MaxGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

import static com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister.*;

public class DatapackRegistry {
    public static void register() {
        DynamicRegistries.registerSynced(EFFECT_REGISTRY_KEY, Effect.CODEC);
        DynamicRegistries.registerSynced(FORM_CHANGE_TOGGLE_INTERACT_REGISTRY_KEY, FormChangeToggleInteractItem.CODEC);
        DynamicRegistries.registerSynced(FORM_CHANGE_INTERACT_REGISTRY_KEY, FormChangeInteractItem.CODEC);
        DynamicRegistries.registerSynced(SOLO_FUSION_REGISTRY_KEY, SoloFusion.CODEC);
        DynamicRegistries.registerSynced(DU_FUSION_REGISTRY_KEY, DuFusion.CODEC);
        DynamicRegistries.registerSynced(GMAX_REGISTRY_KEY, MaxGimmick.CODEC);
        DynamicRegistries.registerSynced(HELD_ITEM_FORM_CHANGE_REGISTRY_KEY, HeldItemFormChange.CODEC);
        DynamicRegistries.registerSynced(MEGA_REGISTRY_KEY, MegaGimmick.CODEC);
        DynamicRegistries.registerSynced(BATTLE_FORM_CHANGE_REGISTRY_KEY, BattleFormChange.CODEC);
        DynamicRegistries.registerSynced(SHOWDOWN_ITEM_REGISTRY_KEY, ShowdownItem.CODEC);
        DynamicRegistries.registerSynced(Z_CRYSTAL_ITEM_REGISTRY_KEY, ZCrystal.CODEC);
        DynamicRegistries.registerSynced(BATTLE_EFFECT_REGISTRY_KEY, BattleEffect.CODEC);
    }
}
