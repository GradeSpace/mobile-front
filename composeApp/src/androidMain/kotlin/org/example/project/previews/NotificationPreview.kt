package org.example.project.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.example.project.features.feed.data.repository.FeedMockRepository
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationScreen
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationState
import org.koin.compose.koinInject

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NotificationScreenPreview() {
    GradeSpaceTheme {
        FeedNotificationScreen(
            state = FeedNotificationState(
                isRefreshing = false,
                error = null,
                notificationItem = FeedMockRepository(
                    koinInject(),
                    koinInject()
                ).localEvents[1]
            ),
            onAction = {}
        )
    }
}