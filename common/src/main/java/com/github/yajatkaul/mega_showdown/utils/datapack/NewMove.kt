package com.github.yajatkaul.mega_showdown.utils.datapack

import com.cobblemon.mod.common.api.moves.MoveTemplate
import com.cobblemon.mod.common.api.moves.animations.ActionEffects
import com.cobblemon.mod.common.api.moves.categories.DamageCategories
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.battles.MoveTarget
import com.cobblemon.mod.common.util.asIdentifierDefaultingNamespace
import com.github.yajatkaul.mega_showdown.mixin.MovesAccessor
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object NewMove {
    fun createMoveTemplate(moveData: JsonObject, id: String): MoveTemplate {
        val num = moveData.get("num").asInt
        val elementalType = ElementalTypes.getOrException(moveData.get("type").asString)
        val damageCategory = DamageCategories.getOrException(moveData.get("category").asString)
        val power = moveData.get("basePower").asDouble
        val target = MoveTarget.fromShowdownId(moveData.get("target").asString)
        val accuracyJson = moveData.get("accuracy").asJsonPrimitive
        val accuracy = if (accuracyJson.isNumber) accuracyJson.asDouble else -1.0
        val pp = moveData.get("pp").asInt
        val priority = moveData.get("priority").asInt
        val critRatio = moveData.get("critRatio")?.asDouble ?: 1.0
        val effectChances = arrayListOf<Double>()
        val secondariesMember = moveData.get("secondaries")
        val secondaryMember = moveData.get("secondary")
        if (secondariesMember != null && secondariesMember is JsonArray) {
            for (j in 0 until secondariesMember.size()) {
                val element = secondariesMember[j].asJsonObject
                if (element.has("chance")) {
                    effectChances += element.get("chance").asDouble
                }
            }
        } else if (secondaryMember != null && secondaryMember is JsonObject) {
            if (secondaryMember.has("chance")) {
                effectChances += secondaryMember.get("chance").asDouble
            }
        }

        val move = MoveTemplate(
            id,
            num,
            elementalType,
            damageCategory,
            power,
            target,
            accuracy,
            pp,
            priority,
            critRatio,
            effectChances.toTypedArray()
        )

        return move
    }

    fun register(move: MoveTemplate) {
        MovesAccessor.getIdMapping()[move.num] = move
        MovesAccessor.getAllMoves()[move.name] = move
    }
}