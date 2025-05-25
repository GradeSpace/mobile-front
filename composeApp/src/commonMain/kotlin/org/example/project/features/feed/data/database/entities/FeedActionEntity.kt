package org.example.project.features.feed.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "feed_actions",
    foreignKeys = [
        ForeignKey(
            entity = FeedEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeedActionEntity(
    @PrimaryKey(autoGenerate = true)
    val actionId: Long = 0,
    val eventId: String,
    val actionType: String,
    val actionName: String,
    val actionTitle: String? = null
)
