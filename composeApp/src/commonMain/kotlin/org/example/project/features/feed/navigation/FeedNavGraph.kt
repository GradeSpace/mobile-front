package org.example.project.features.feed.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.utils.NavigationManager

fun NavGraphBuilder.feedNavGraph(navigationManager: NavigationManager) {
    composable<FeedRoute.FeedEventMain> {
        Text(text = "hello")
    }
}