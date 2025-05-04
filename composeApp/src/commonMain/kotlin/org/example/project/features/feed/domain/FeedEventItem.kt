package org.example.project.features.feed.domain

import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User

data class FeedEventItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val author: User,
    val dateTime: LocalDateTime,
    val attachments: List<Attachment> = emptyList(),
    val actions: List<FeedAction> = emptyList()
)
