package org.example.project.features.tasks.data.database.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.example.project.features.tasks.data.database.entities.TaskAttachmentEntity
import org.example.project.features.tasks.data.database.entities.TaskEventEntity
import org.example.project.features.tasks.data.database.entities.TaskVariantEntity

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: TaskAttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<TaskAttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariant(variant: TaskVariantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<TaskVariantEntity>)

    @Query("SELECT * FROM task_events ORDER BY lastUpdateDateTime DESC")
    fun getAllTasks(): Flow<List<TaskEventEntity>>

    @Query("SELECT * FROM task_events WHERE taskStatusType = :statusType ORDER BY lastUpdateDateTime DESC")
    fun getTasksByStatus(statusType: String): Flow<List<TaskEventEntity>>

    @Query("SELECT * FROM task_events WHERE id = :taskId")
    fun getTaskById(taskId: String): Flow<TaskEventEntity?>

    @Query("SELECT * FROM task_attachments WHERE taskId = :taskId")
    fun getAttachmentsForTask(taskId: String): Flow<List<TaskAttachmentEntity>>

    @Query("SELECT * FROM task_variants WHERE taskId = :taskId ORDER BY varNum")
    fun getVariantsForTask(taskId: String): Flow<List<TaskVariantEntity>>

    @Query("DELETE FROM task_events")
    suspend fun clearAllTasks()

    @Transaction
    @Query("SELECT * FROM task_events ORDER BY lastUpdateDateTime DESC")
    fun getAllTasksWithRelations(): Flow<List<TaskWithRelations>>

    @Transaction
    @Query("SELECT * FROM task_events WHERE id = :taskId")
    fun getTaskWithRelations(taskId: String): Flow<TaskWithRelations?>

    data class TaskWithRelations(
        @Embedded val task: TaskEventEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "taskId"
        )
        val attachments: List<TaskAttachmentEntity>,
        @Relation(
            parentColumn = "id",
            entityColumn = "taskId"
        )
        val variants: List<TaskVariantEntity>
    )

    @Transaction
    suspend fun insertTaskWithRelations(
        task: TaskEventEntity,
        attachments: List<TaskAttachmentEntity>,
        variants: List<TaskVariantEntity>
    ) {
        insertTask(task)
        if (attachments.isNotEmpty()) {
            insertAttachments(attachments)
        }
        if (variants.isNotEmpty()) {
            insertVariants(variants)
        }
    }
}
