package org.example.project.features.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.LocalNavigationManager
import org.example.project.app.LocalUiEventsManager
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationScreenRoot
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationViewModel
import org.example.project.features.feed.presentation.notification_create.NotificationCreateRootScreen
import org.example.project.features.feed.presentation.notification_create.NotificationCreateViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.feedNavGraph() {
    composable<FeedRoute.FeedNotification> {
        val navigationManager = LocalNavigationManager.current
        val viewModel = koinViewModel<FeedNotificationViewModel>()
        FeedNotificationScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager
        )
    }

    composable<FeedRoute.NotificationCreate> {
        val navigationManager = LocalNavigationManager.current
        val uiEventsManager = LocalUiEventsManager.current
        val viewModel = koinViewModel<NotificationCreateViewModel>()
        NotificationCreateRootScreen(
            viewModel,
            navigationManager,
            uiEventsManager
        )
    }
}