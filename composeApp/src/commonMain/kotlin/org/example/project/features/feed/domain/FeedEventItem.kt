package org.example.project.features.feed.domain

import kotlinx.datetime.LocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventItem
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText

data class FeedEventItem(
    override val id: String,
    override val title: String,
    override val description: UiText = UiText.StringResourceId(Res.string.no_description),
    override val author: User,
    override val lastUpdateDateTime: LocalDateTime,
    override val attachments: List<Attachment> = emptyList(),
    override val receivers: List<String> = emptyList(),
    override val location: EventLocation? = null,
    val actions: List<FeedAction> = emptyList(),
) : EventItem
