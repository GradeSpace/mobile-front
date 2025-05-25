package org.example.project.features.lessons.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.database.converters.LocalDateTimeConverter
import org.example.project.core.data.database.converters.StringListTypeConverter

@Entity(tableName = "lesson_events")
data class LessonEventEntity(
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
    val locationLessonUrl: String? = null,

    // Поля, специфичные для занятий
    val subject: String,
    @TypeConverters(LocalDateTimeConverter::class)
    val startTime: LocalDateTime,
    @TypeConverters(LocalDateTimeConverter::class)
    val endTime: LocalDateTime? = null,

    // Статусы
    val attendanceStatusType: String, // "NOT_ATTENDED", "ATTENDANCE_ON_CHECK", "ATTENDED"
    val lessonStatusType: String // "NOT_STARTED", "IN_PROGRESS", "FINISHED"
)
