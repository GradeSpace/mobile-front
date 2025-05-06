package org.example.project.features.feed.domain

import kotlinx.datetime.LocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText

data class FeedEventItem(
    val id: String,
    val title: String,
    val description: UiText = UiText.StringResourceId(Res.string.no_description),
    val author: User,
    val dateTime: LocalDateTime,
    val attachments: List<Attachment> = emptyList(),
    val actions: List<FeedAction> = emptyList(),
    val receivers: List<String> = emptyList()
)
