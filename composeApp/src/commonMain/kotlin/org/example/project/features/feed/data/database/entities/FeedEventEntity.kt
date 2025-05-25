package org.example.project.features.feed.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.database.converters.LocalDateTimeConverter
import org.example.project.core.data.database.converters.StringListTypeConverter

@Entity(tableName = "feed_events")
data class FeedEventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val authorId: String,
    val authorName: String,
    val authorSurname: String,
    val authorMiddleName: String?,
    val authorImageUrl: String?,
    @TypeConverters(LocalDateTimeConverter::class)
    val lastUpdateDateTime: LocalDateTime,
    @TypeConverters(StringListTypeConverter::class)
    val receivers: List<String> = emptyList(),
    val locationCabinet: String? = null,
    val locationLessonUrl: String? = null
)
