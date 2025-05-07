package org.example.project.core.data.model.event

import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText

interface EventItem {
    val id: String
    val title: String
    val description: UiText
    val author: User
    val lastUpdateDateTime: LocalDateTime
    val attachments: List<Attachment>
    val receivers: List<String>
}