package com.github.yajatkaul.mega_showdown.utils.datapack

import com.cobblemon.mod.common.api.abilities.AbilityTemplate

object NewAbility {
    fun getAbility(name: String): AbilityTemplate {
        return AbilityTemplate(name)
    }
}