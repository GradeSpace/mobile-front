package org.example.project.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.LocalNavigationManager
import org.example.project.app.LocalUiEventsManager
import org.example.project.features.auth.presentation.auth_screen.AuthScreenRoot
import org.example.project.features.auth.presentation.auth_screen.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authNavGraph() {
    composable<AuthRoute.AuthScreen> {
        val navigationManager = LocalNavigationManager.current
        val uiEventsManager = LocalUiEventsManager.current
        val viewModel = koinViewModel<AuthViewModel>()
        AuthScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            uiEventsManager = uiEventsManager
        )
    }
}