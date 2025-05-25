package org.example.project.features.lessons.data.mappers

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText
import org.example.project.features.lessons.data.database.entities.LessonAttachmentEntity
import org.example.project.features.lessons.data.database.entities.LessonEventEntity
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonEventItem
import org.example.project.features.lessons.domain.LessonStatus

// Конвертация из доменной модели в сущность БД
fun LessonEventItem.toEntity(): LessonEventEntity {
    return LessonEventEntity(
        id = id,
        title = title,
        description = when (description) {
            is UiText.DynamicString -> description.value
            is UiText.StringResourceId -> "" // Обработаем при обратном преобразовании
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

        // Поля для занятий
        subject = subject,
        startTime = startTime,
        endTime = endTime,

        // Статусы
        attendanceStatusType = when (attendanceStatus) {
            is AttendanceStatus.NotAttended -> "NOT_ATTENDED"
            is AttendanceStatus.AttendanceOnCheck -> "ATTENDANCE_ON_CHECK"
            is AttendanceStatus.Attended -> "ATTENDED"
        },
        lessonStatusType = when (lessonStatus) {
            is LessonStatus.NotStarted -> "NOT_STARTED"
            is LessonStatus.InProgress -> "IN_PROGRESS"
            is LessonStatus.Finished -> "FINISHED"
        }
    )
}

// Конвертация из сущности БД в доменную модель
fun LessonEventEntity.toDomain(
    attachments: List<LessonAttachmentEntity> = emptyList()
): LessonEventItem {
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

    val location = if (locationCabinet != null || locationLessonUrl != null) {
        EventLocation(
            cabinet = locationCabinet,
            lessonUrl = locationLessonUrl
        )
    } else {
        null
    }

    // Преобразуем статусы
    val attendanceStatus = when (attendanceStatusType) {
        "NOT_ATTENDED" -> AttendanceStatus.NotAttended
        "ATTENDANCE_ON_CHECK" -> AttendanceStatus.AttendanceOnCheck
        "ATTENDED" -> AttendanceStatus.Attended
        else -> AttendanceStatus.NotAttended
    }

    val lessonStatus = when (lessonStatusType) {
        "NOT_STARTED" -> LessonStatus.NotStarted
        "IN_PROGRESS" -> LessonStatus.InProgress
        "FINISHED" -> LessonStatus.Finished
        else -> LessonStatus.NotStarted
    }

    return LessonEventItem(
        id = id,
        title = title,
        description = domainDescription,
        author = author,
        lastUpdateDateTime = lastUpdateDateTime,
        attachments = domainAttachments,
        receivers = receivers,
        location = location,
        subject = subject,
        startTime = startTime,
        endTime = endTime,
        attendanceStatus = attendanceStatus,
        lessonStatus = lessonStatus
    )
}

// Конвертация вложения из доменной модели в сущность БД
fun Attachment.toLessonEntity(lessonId: String): LessonAttachmentEntity? {
    return when (this) {
        is Attachment.FileAttachment -> LessonAttachmentEntity(
            id = id,
            lessonId = lessonId,
            url = url,
            fileName = fileName,
            fileSize = fileSize,
            fileType = fileType.name
        )
        else -> null
    }
}

// Конвертация вложения из сущности БД в доменную модель
fun LessonAttachmentEntity.toDomain(): Attachment {
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
