package org.example.project.core.data.model

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.english
import mobile_front.composeapp.generated.resources.russian
import org.example.project.core.presentation.UiText

enum class Language(val id: Int) {
    RUSSIAN(0),
    ENGLISH(1);

    fun toUiText(): UiText {
        return when(this) {
            RUSSIAN -> UiText.StringResourceId(Res.string.russian)
            ENGLISH -> UiText.StringResourceId(Res.string.english)
        }
    }

    fun toIso(): String {
        return when(this) {
            Language.RUSSIAN -> "ru"
            Language.ENGLISH -> "en"
        }
    }
}