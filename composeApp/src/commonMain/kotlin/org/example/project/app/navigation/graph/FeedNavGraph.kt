package org.example.project.app.navigation.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.FeedRoutes
import org.example.project.app.navigation.route.Graph
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager

fun NavGraphBuilder.feedNavGraph(navigationManager: NavigationManager) {
    composable<FeedRoutes.FeedMain> {
        Text(text = "hello")
    }
}