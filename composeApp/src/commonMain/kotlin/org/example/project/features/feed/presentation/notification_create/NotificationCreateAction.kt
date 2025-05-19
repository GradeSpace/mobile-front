package org.example.project.features.feed.presentation.notification_create

import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.presentation.AttachmentSource

sealed interface NotificationCreateAction {
    data object OnBackClick : NotificationCreateAction
    data object OnSendClick : NotificationCreateAction
    data class OnTitleChange(val title: String) : NotificationCreateAction
    data class OnDescriptionChange(val description: String) : NotificationCreateAction
    data class OnReceiverSelected(val receiver: String) : NotificationCreateAction
    data class OnReceiverDeselected(val receiver: String) : NotificationCreateAction
    data class OnSourceSelected(val source: AttachmentSource) : NotificationCreateAction
    data class OnRemoveAttachment(val attachment: Attachment) : NotificationCreateAction
}