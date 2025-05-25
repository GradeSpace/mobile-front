package org.example.project.features.feed.presentation.notification_create

import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.presentation.UiText

data class NotificationCreateState(
    val title: String = "",
    val titleError: UiText? = null,

    val description: String = "",
    val descriptionError: UiText? = null,

    val attachments: List<Attachment> = emptyList(),
    val pickedReceivers: List<String> = emptyList(),
    val availableReceivers: List<String> = emptyList(),

    val isBottomSheetAttachmentVisible: Boolean = false,
    val error: UiText? = null
)