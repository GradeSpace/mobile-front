package org.example.project.features.lessons.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "lesson_attachments",
    foreignKeys = [
        ForeignKey(
            entity = LessonEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LessonAttachmentEntity(
    @PrimaryKey
    val id: String,
    val lessonId: String,
    val url: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String // Будет хранить имя enum FileType
)
