package org.example.project.features.feed.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedEventEntity(
    @PrimaryKey(autoGenerate = false) val id: String
)
