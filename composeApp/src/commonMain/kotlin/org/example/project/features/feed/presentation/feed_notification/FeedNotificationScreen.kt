package org.example.project.features.feed.presentation.feed_notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.presentation.ui.common.EventAttachments
import org.example.project.core.presentation.ui.common.EventDescription
import org.example.project.features.feed.presentation.feed_notification.components.EventReceivers
import org.example.project.features.feed.presentation.feed_notification.components.NotificationBottomBar
import org.example.project.features.feed.presentation.feed_notification.components.NotificationTopBar

@Composable
fun FeedNotificationScreenRoot(
    viewModel: FeedNotificationViewModel,
    navigationManager: NavigationManager
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is FeedNotificationNavigationEvent.NavigateBack ->
                    navigationManager.navigateBack()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    FeedNotificationScreen(state, viewModel::onAction)
}

@Composable
fun FeedNotificationScreen(
    state: FeedNotificationState,
    onAction: (FeedNotificationAction) -> Unit
) {
    if (state.notificationItem == null) {
        CircularProgressIndicator()
    } else {
        Scaffold(
            bottomBar = {
                NotificationBottomBar(
                    state.notificationItem.actions,
                )
            },
            topBar = {
                NotificationTopBar(state.notificationItem) {
                    onAction(FeedNotificationAction.OnBackClick)
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val scrollState = rememberScrollState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp, top = 8.dp)
            ) {
                EventReceivers(
                    state.notificationItem.receivers,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                EventDescription(
                    state.notificationItem.description,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                EventAttachments(
                    state.notificationItem.attachments,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}