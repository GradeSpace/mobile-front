package org.example.project.core.data.model.user

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.student
import mobile_front.composeapp.generated.resources.teacher
import mobile_front.composeapp.generated.resources.user
import org.example.project.core.presentation.UiText

sealed interface UserRole {
    val index: Int

    data object Student : UserRole {
        override val index: Int = 0
    }

    data object Teacher : UserRole {
        override val index: Int = 1
    }

    data object Undefined : UserRole {
        override val index: Int = -1
    }

    fun toUiText(): UiText {
        return when (this) {
            Student -> UiText.StringResourceId(Res.string.student)
            Teacher -> UiText.StringResourceId(Res.string.teacher)
            Undefined -> UiText.StringResourceId(Res.string.user)
        }
    }

    companion object {
        fun fromIndex(index: Int): UserRole {
            return when (index) {
                0 -> Student
                1 -> Teacher
                else -> Undefined
            }
        }
    }
}
