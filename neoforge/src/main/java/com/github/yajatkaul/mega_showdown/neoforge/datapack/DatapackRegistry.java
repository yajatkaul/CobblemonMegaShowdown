package com.github.yajatkaul.mega_showdown.neoforge.datapack;

import com.github.yajatkaul.mega_showdown.api.codec.*;
import com.github.yajatkaul.mega_showdown.gimmick.MaxGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import static com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister.*;

public class DatapackRegistry {
    public static void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(EFFECT_REGISTRY_KEY, Effect.CODEC, Effect.CODEC);
        event.dataPackRegistry(FORM_CHANGE_TOGGLE_INTERACT_REGISTRY_KEY, FormChangeToggleInteractItem.CODEC, FormChangeToggleInteractItem.CODEC);
        event.dataPackRegistry(FORM_CHANGE_INTERACT_REGISTRY_KEY, FormChangeInteractItem.CODEC, FormChangeInteractItem.CODEC);
        event.dataPackRegistry(SOLO_FUSION_REGISTRY_KEY, SoloFusion.CODEC, SoloFusion.CODEC);
        event.dataPackRegistry(DU_FUSION_REGISTRY_KEY, DuFusion.CODEC, DuFusion.CODEC);
        event.dataPackRegistry(GMAX_REGISTRY_KEY, MaxGimmick.CODEC, MaxGimmick.CODEC);
        event.dataPackRegistry(HELD_ITEM_FORM_CHANGE_REGISTRY_KEY, HeldItemFormChange.CODEC, HeldItemFormChange.CODEC);
        event.dataPackRegistry(MEGA_REGISTRY_KEY, MegaGimmick.CODEC, MegaGimmick.CODEC);
        event.dataPackRegistry(SHOWDOWN_ITEM_REGISTRY_KEY, ShowdownItem.CODEC, ShowdownItem.CODEC);
        event.dataPackRegistry(BATTLE_FORM_CHANGE_REGISTRY_KEY, BattleFormChange.CODEC, BattleFormChange.CODEC);
        event.dataPackRegistry(Z_CRYSTAL_ITEM_REGISTRY_KEY, ZCrystal.CODEC, ZCrystal.CODEC);
        event.dataPackRegistry(BATTLE_EFFECT_REGISTRY_KEY, BattleEffect.CODEC, BattleEffect.CODEC);
    }
}
