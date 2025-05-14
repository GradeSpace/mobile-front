package org.example.project.core.data.model.user

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.student
import mobile_front.composeapp.generated.resources.user
import org.example.project.core.presentation.UiText

sealed interface UserRole {
    data object Student : UserRole
    data object Tutor : UserRole
    data object Undefined : UserRole

    fun toUiText(): UiText {
        return when (this) {
            Student -> UiText.StringResourceId(Res.string.student)
            Tutor -> UiText.StringResourceId(Res.string.student)
            Undefined -> UiText.StringResourceId(Res.string.user)
        }
    }
}