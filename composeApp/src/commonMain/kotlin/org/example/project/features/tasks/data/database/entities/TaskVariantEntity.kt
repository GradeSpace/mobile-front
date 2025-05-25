package org.example.project.features.tasks.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.example.project.core.data.database.converters.StringListTypeConverter

@Entity(
    tableName = "task_variants",
    foreignKeys = [
        ForeignKey(
            entity = TaskEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskVariantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: String,
    val varNum: Int,
    val text: String?,
    @TypeConverters(StringListTypeConverter::class)
    val receivers: List<String>? = null
)
