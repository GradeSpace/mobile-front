package org.example.project.features.profile.presentation.profile_main

import org.example.project.core.data.model.Language
import org.example.project.core.data.model.Theme

sealed interface ProfileAction {
    data object OnPullToRefresh : ProfileAction
    data object ShowThemeBottomSheet : ProfileAction
    data object ShowLanguageBottomSheet : ProfileAction
    data object HideBottomSheet : ProfileAction
    data class ChangeTheme(val theme: Theme) : ProfileAction
    data class ChangeLanguage(val language: Language) : ProfileAction
    data object ExitApp : ProfileAction
}