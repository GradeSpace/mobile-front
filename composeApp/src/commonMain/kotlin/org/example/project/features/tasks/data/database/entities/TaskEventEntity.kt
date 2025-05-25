package org.example.project.features.tasks.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.database.converters.LocalDateTimeConverter
import org.example.project.core.data.database.converters.StringListTypeConverter

@Entity(tableName = "task_events")
data class TaskEventEntity(
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

    // Поля, специфичные для заданий
    val minGrade: Double? = null,
    val maxGrade: Double? = null,
    val currentGrade: Double? = null,

    @TypeConverters(LocalDateTimeConverter::class)
    val deadLine: LocalDateTime? = null,

    val taskStatusType: String, // "NOT_ISSUED", "ISSUED", "UNDER_CHECK", "REJECTED", "COMPLETED"
    @TypeConverters(LocalDateTimeConverter::class)
    val taskStatusDateTime: LocalDateTime? = null,
    val rejectionReason: String? = null,

    val variantDistributionMode: Int = 0 // 0 - NONE, 1 - RANDOM, 2 - VAR_TO_RECEIVER
)
