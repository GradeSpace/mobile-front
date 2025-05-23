package org.example.project.features.lessons.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventLocation

data class LessonCreateDraft(
    val title: String? = null,
    val subject: String? = null,
    val description: String? = null,
    val receivers: List<String>? = null,
    val attachments: List<Attachment>? = null,
    val lessonDate: LocalDate? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val isOnlineLocationEnabled: Boolean = false,
    val isOfflineLocationEnabled: Boolean = false,
    val onlineLink: String = "",
    val offlinePlace: String = "",
    val location: EventLocation? = null
)
