package org.example.project.features.feed.presentation.feed_notification

sealed interface FeedNotificationNavigationEvent {
    data object NavigateBack : FeedNotificationNavigationEvent
}