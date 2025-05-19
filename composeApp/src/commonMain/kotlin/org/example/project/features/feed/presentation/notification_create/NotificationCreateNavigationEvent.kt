package org.example.project.features.feed.presentation.notification_create

interface NotificationCreateNavigationEvent {
    data object NavigateBack : NotificationCreateNavigationEvent
    data object OpenAttachmentPicker : NotificationCreateNavigationEvent
}