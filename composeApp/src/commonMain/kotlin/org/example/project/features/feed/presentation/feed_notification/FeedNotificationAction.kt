package org.example.project.features.feed.presentation.feed_notification

sealed interface FeedNotificationAction {
    data object OnBackClick : FeedNotificationAction
    data object OnPullToRefresh : FeedNotificationAction
    data class OnAttachmentClick(val url: String): FeedNotificationAction
}