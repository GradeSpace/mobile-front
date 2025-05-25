package org.example.project.features.lessons.presentation.lesson_create

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.presentation.UiText

data class LessonCreateState(
    val title: String = "",
    val titleError: UiText? = null,

    val description: String = "",
    val descriptionError: UiText? = null,

    val subject: String = "",
    val subjectError: UiText? = null,

    val attachments: List<Attachment> = emptyList(),
    val pickedReceivers: List<String> = emptyList(),
    val availableReceivers: List<String> = emptyList(),

    val lessonDate: LocalDate? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,

    val isOnlineLocation: Boolean = false,
    val isOfflineLocation: Boolean = false,
    val onlineLink: String = "",
    val offlinePlace: String = "",
    val locationError: UiText? = null,

    val isBottomSheetAttachmentVisible: Boolean = false,
    val error: UiText? = null
)
