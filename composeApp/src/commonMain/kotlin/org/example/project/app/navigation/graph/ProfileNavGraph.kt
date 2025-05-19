package org.example.project.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.LocalNavigationManager
import org.example.project.app.navigation.route.ProfileRoutes

fun NavGraphBuilder.profileNavGraph() {
    composable<ProfileRoutes.ProfileMain> {
        val navigationManager = LocalNavigationManager.current

    }
}