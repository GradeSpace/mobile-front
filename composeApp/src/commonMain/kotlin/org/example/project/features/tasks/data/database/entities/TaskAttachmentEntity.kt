package org.example.project.features.tasks.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "task_attachments",
    foreignKeys = [
        ForeignKey(
            entity = TaskEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskAttachmentEntity(
    @PrimaryKey
    val id: String,
    val taskId: String,
    val url: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String
)
