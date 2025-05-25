package org.example.project.features.feed.presentation.notification_create

import org.example.project.core.data.model.attachment.Attachment

interface NotificationCreateNavigationEvent {
    data object NavigateBack : NotificationCreateNavigationEvent
    data class OpenFile(
        val attachment: Attachment
    ) : NotificationCreateNavigationEvent
}