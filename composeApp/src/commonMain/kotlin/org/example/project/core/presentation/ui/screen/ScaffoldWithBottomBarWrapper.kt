package org.example.project.core.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.presentation.ui.screen.components.AppBottomBar

@Composable
fun ScaffoldWithBottomBarWrapper(
    navigationManager: NavigationManager,
    snackbarHostState: SnackbarHostState,
    tabs: List<TabRoute>,
    withBottomBar: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            AppBottomBar(
                navigationManager, tabs
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content(innerPadding)
        }
    }
}