package org.example.project.features.feed.presentation.feed_notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.Platform
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.app.navigation.utils.openFile
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.EventAttachments
import org.example.project.core.presentation.ui.common.EventDescription
import org.example.project.core.presentation.ui.common.EventTopBar
import org.example.project.features.feed.presentation.feed_notification.components.EventReceivers
import org.example.project.features.feed.presentation.feed_notification.components.NotificationBottomBar
import org.example.project.getPlatform

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

                is FeedNotificationNavigationEvent.OpenFile ->
                    navigationManager.openFile(navEvent.url)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    FeedNotificationScreen(state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedNotificationScreen(
    state: FeedNotificationState,
    onAction: (FeedNotificationAction) -> Unit
) {

    Scaffold(
        bottomBar = {
            NotificationBottomBar(
                state.notificationItem?.actions ?: emptyList(),
            )
        },
        topBar = {
            EventTopBar(
                event = state.notificationItem,
                onNavigateBackClick = { onAction(FeedNotificationAction.OnBackClick) }
            ) {
                if (getPlatform() == Platform.Desktop) {
                    DesktopRefreshButton { onAction(FeedNotificationAction.OnPullToRefresh) }
                } else {
                    Spacer(modifier = Modifier.widthIn(48.dp))
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(FeedNotificationAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.notificationItem == null) return@PullToRefreshBox
            val scrollState = rememberScrollState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
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
                    state.notificationItem.lastUpdateDateTime,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                EventAttachments(
                    state.notificationItem.attachments,
                    onClick = {
                        onAction(FeedNotificationAction.OnAttachmentClick(it))
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}