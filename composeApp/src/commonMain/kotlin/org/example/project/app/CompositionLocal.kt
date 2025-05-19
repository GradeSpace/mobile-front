package org.example.project.app

import androidx.compose.runtime.staticCompositionLocalOf
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.presentation.UiEventsManager

val LocalNavigationManager = staticCompositionLocalOf<NavigationManager> { error("No NavigationManager provided") }
val LocalUiEventsManager = staticCompositionLocalOf<UiEventsManager> { error("No UiEventsManager provided") }