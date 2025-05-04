package org.example.project.core.data.database.entities.feed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedEventEntity(
    @PrimaryKey(autoGenerate = false) val id: String
)