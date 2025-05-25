package org.example.project.features.tasks.data.mappers

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.data.model.note.Grade
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText
import org.example.project.features.tasks.data.database.entities.TaskAttachmentEntity
import org.example.project.features.tasks.data.database.entities.TaskEventEntity
import org.example.project.features.tasks.data.database.entities.TaskVariantEntity
import org.example.project.features.tasks.domain.TaskEventItem
import org.example.project.features.tasks.domain.TaskStatus
import org.example.project.features.tasks.domain.TaskVariant

// Конвертация из доменной модели в сущность БД
fun TaskEventItem.toEntity(): TaskEventEntity {
    return TaskEventEntity(
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

        // Поля для оценок
        minGrade = grade?.minGrade?.value,
        maxGrade = grade?.maxGrade?.value,
        currentGrade = grade?.currentGrade?.value,

        // Дедлайн
        deadLine = deadLine,

        // Статус задания
        taskStatusType = when (status) {
            is TaskStatus.NotIssued -> "NOT_ISSUED"
            is TaskStatus.Issued -> "ISSUED"
            is TaskStatus.UnderCheck -> "UNDER_CHECK"
            is TaskStatus.Rejected -> "REJECTED"
            is TaskStatus.Completed -> "COMPLETED"
            null -> "NOT_ISSUED"
        },
        taskStatusDateTime = status?.dateTime,
        rejectionReason = (status as? TaskStatus.Rejected)?.reason
    )
}

// Конвертация из сущности БД в доменную модель
fun TaskEventEntity.toDomain(
    attachments: List<TaskAttachmentEntity> = emptyList(),
    variants: List<TaskVariantEntity> = emptyList()
): TaskEventItem {
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

    // Создаем объект GradeRange
    val gradeRange = if (maxGrade != null) {
        GradeRange(
            minGrade = minGrade?.let { Grade(it) },
            maxGrade = Grade(maxGrade),
            currentGrade = currentGrade?.let { Grade(it) }
        )
    } else {
        null
    }

    // Создаем объект TaskStatus
    val taskStatus = when (taskStatusType) {
        "NOT_ISSUED" -> TaskStatus.NotIssued(taskStatusDateTime)
        "ISSUED" -> TaskStatus.Issued(taskStatusDateTime)
        "UNDER_CHECK" -> TaskStatus.UnderCheck(taskStatusDateTime)
        "REJECTED" -> TaskStatus.Rejected(taskStatusDateTime, rejectionReason)
        "COMPLETED" -> TaskStatus.Completed(taskStatusDateTime)
        else -> TaskStatus.NotIssued(taskStatusDateTime)
    }

    return TaskEventItem(
        id = id,
        title = title,
        description = domainDescription,
        author = author,
        lastUpdateDateTime = lastUpdateDateTime,
        attachments = domainAttachments,
        receivers = receivers,
        location = location,
        grade = gradeRange,
        deadLine = deadLine,
        status = taskStatus
    )
}

// Конвертация вложения из доменной модели в сущность БД
fun Attachment.toTaskEntity(taskId: String): TaskAttachmentEntity? {
    return when (this) {
        is Attachment.FileAttachment -> TaskAttachmentEntity(
            id = id,
            taskId = taskId,
            url = url,
            fileName = fileName,
            fileSize = fileSize,
            fileType = fileType.name
        )
        else -> null
    }
}

// Конвертация вложения из сущности БД в доменную модель
fun TaskAttachmentEntity.toDomain(): Attachment {
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

// Конвертация варианта из доменной модели в сущность БД
fun TaskVariant.toEntity(taskId: String): TaskVariantEntity {
    return TaskVariantEntity(
        taskId = taskId,
        varNum = varNum,
        text = text,
        receivers = receivers
    )
}

// Конвертация варианта из сущности БД в доменную модель
fun TaskVariantEntity.toDomain(): TaskVariant {
    return TaskVariant(
        varNum = varNum,
        text = text,
        receivers = receivers
    )
}
