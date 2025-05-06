package org.example.project.features.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationScreenRoot
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.feedNavGraph(navigationManager: NavigationManager) {
    composable<FeedRoute.FeedNotification> {
        val viewModel = koinViewModel<FeedNotificationViewModel>()
        FeedNotificationScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager
        )
    }
}