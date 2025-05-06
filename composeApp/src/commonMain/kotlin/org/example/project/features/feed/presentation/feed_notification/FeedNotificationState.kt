package org.example.project.features.feed.presentation.feed_notification

import org.example.project.core.presentation.UiText
import org.example.project.features.feed.domain.FeedEventItem

data class FeedNotificationState(
    val isLoading: Boolean = true,
    var error: UiText? = null,
    val notificationItem: FeedEventItem? = null
)
