package org.example.project.features.feed.data.mappers

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText
import org.example.project.features.feed.data.database.entities.AttachmentEntity
import org.example.project.features.feed.data.database.entities.FeedActionEntity
import org.example.project.features.feed.data.database.entities.FeedEventEntity
import org.example.project.features.feed.domain.FeedAction
import org.example.project.features.feed.domain.FeedEventItem

// Convert domain model to entity
fun FeedEventItem.toEntity(blockId: Int = 0): FeedEventEntity {
    return FeedEventEntity(
        id = id,
        title = title,
        description = when (description) {
            is UiText.DynamicString -> description.value
            is UiText.StringResourceId -> "" // We'll handle this in the reverse mapping
            else -> ""
        },
        authorId = author.uid ?: "",
        authorName = author.name,
        authorSurname = author.surname,
        authorMiddleName = author.middleName,
        authorImageUrl = author.imageUrl,
        lastUpdateDateTime = lastUpdateDateTime,
        receivers = receivers,
        locationCabinet = location?.cabinet,
        locationLessonUrl = location?.lessonUrl,
    )
}

// Convert entity to domain model
fun FeedEventEntity.toDomain(
    attachments: List<AttachmentEntity> = emptyList(),
    actions: List<FeedActionEntity> = emptyList()
): FeedEventItem {
    val author = User(
        uid = authorId,
        name = authorName,
        surname = authorSurname,
        middleName = authorMiddleName,
        imageUrl = authorImageUrl
    )

    val domainDescription = if (description.isNotEmpty()) {
        UiText.DynamicString(description)
    } else {
        UiText.StringResourceId(Res.string.no_description)
    }

    val domainAttachments = attachments.map { it.toDomain() }
    val domainActions = actions.map { it.toDomain() }

    val location = if (locationCabinet != null || locationLessonUrl != null) {
        EventLocation(
            cabinet = locationCabinet,
            lessonUrl = locationLessonUrl
        )
    } else {
        null
    }

    return FeedEventItem(
        id = id,
        title = title,
        description = domainDescription,
        author = author,
        lastUpdateDateTime = lastUpdateDateTime,
        attachments = domainAttachments,
        receivers = receivers,
        location = location,
        actions = domainActions
    )
}

// Convert domain attachment to entity
fun Attachment.toEntity(eventId: String): AttachmentEntity? {
    return when (this) {
        is Attachment.FileAttachment -> AttachmentEntity(
            id = id,
            eventId = eventId,
            url = url,
            fileName = fileName,
            fileSize = fileSize,
            fileType = fileType.name
        )
        else -> null
    }
}

// Convert entity to domain attachment
fun AttachmentEntity.toDomain(): Attachment {
    val fileType = try {
        Attachment.FileType.valueOf(fileType)
    } catch (e: Exception) {
        Attachment.FileType.UNKNOWN
    }

    return Attachment.FileAttachment(
        id = id,
        url = url,
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType
    )
}

// Convert domain action to entity
fun FeedAction.toEntity(eventId: String): FeedActionEntity {
    return when (this) {
        is FeedAction.ButtonAction -> FeedActionEntity(
            eventId = eventId,
            actionType = "BUTTON",
            actionName = actionName
        )
        is FeedAction.PerformedAction -> FeedActionEntity(
            eventId = eventId,
            actionType = "PERFORMED",
            actionName = "",
            actionTitle = title
        )
    }
}

// Convert entity to domain action
fun FeedActionEntity.toDomain(): FeedAction {
    return when (actionType) {
        "BUTTON" -> FeedAction.ButtonAction(
            actionName = actionName,
            action = { }  // Note: We can't store the actual action in the database
        )
        "PERFORMED" -> FeedAction.PerformedAction(
            title = actionTitle ?: ""
        )
        else -> FeedAction.PerformedAction(title = "Unknown action")
    }
}
