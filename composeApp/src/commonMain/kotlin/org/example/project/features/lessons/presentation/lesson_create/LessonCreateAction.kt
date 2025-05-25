package org.example.project.features.lessons.presentation.lesson_create

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.presentation.AttachmentSource

sealed interface LessonCreateAction {
    data object OnBackClick : LessonCreateAction
    data object OnSendClick : LessonCreateAction
    data class OnTitleChange(val title: String) : LessonCreateAction
    data class OnDescriptionChange(val description: String) : LessonCreateAction
    data class OnSubjectChange(val subject: String) : LessonCreateAction
    data class OnReceiverSelected(val receiver: String) : LessonCreateAction
    data class OnReceiverDeselected(val receiver: String) : LessonCreateAction
    data class OnSourceSelected(val source: AttachmentSource) : LessonCreateAction
    data class OnRemoveAttachment(val attachment: Attachment) : LessonCreateAction
    data class OnAttachmentClick(val attachment: Attachment) : LessonCreateAction

    data class OnLessonDateChange(val date: LocalDate?) : LessonCreateAction
    data class OnStartTimeChange(val time: LocalDateTime?) : LessonCreateAction
    data class OnEndTimeChange(val time: LocalDateTime?) : LessonCreateAction

    data class OnOnlineLocationChange(val isEnabled: Boolean) : LessonCreateAction
    data class OnOfflineLocationChange(val isEnabled: Boolean) : LessonCreateAction
    data class OnOnlineLinkChange(val link: String) : LessonCreateAction
    data class OnOfflinePlaceChange(val place: String) : LessonCreateAction
}
