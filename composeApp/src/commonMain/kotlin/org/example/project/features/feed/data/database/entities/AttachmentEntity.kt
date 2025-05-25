package org.example.project.features.feed.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "feed_attachments",
    foreignKeys = [
        ForeignKey(
            entity = FeedEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttachmentEntity(
    @PrimaryKey
    val id: String,
    val eventId: String,
    val url: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String
)
