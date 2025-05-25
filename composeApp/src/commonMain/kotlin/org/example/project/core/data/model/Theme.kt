package org.example.project.core.data.model

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.dark_mode
import mobile_front.composeapp.generated.resources.light_mode
import mobile_front.composeapp.generated.resources.system_mode
import org.example.project.core.presentation.UiText

enum class Theme(val id: Int) {
    LIGHT(0), DARK(1), SYSTEM(2);

    fun toUiText(): UiText {
        return when(this) {
            LIGHT -> UiText.StringResourceId(Res.string.light_mode)
            DARK -> UiText.StringResourceId(Res.string.dark_mode)
            SYSTEM -> UiText.StringResourceId(Res.string.system_mode)
        }
    }
}