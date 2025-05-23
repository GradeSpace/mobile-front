package org.example.project.features.tasks.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.features.tasks.domain.TasksEventsBlock.BlockType

interface TasksRepository {
    fun fetchTasksEvents(filter: Set<BlockType> = BlockType.entries.toSet()): Flow<List<TasksEventsBlock>>

    fun fetchReceivers(): Flow<List<String>>

    fun getTask(eventId: String?): Flow<TaskEventItem?>

    suspend fun actualizeTasks(): EmptyResult<DataError.Remote>

    suspend fun actualizeTask(eventId: String?): EmptyResult<DataError.Remote>

    suspend fun createTask(event: TaskEventItem): EmptyResult<DataError>

    suspend fun getCreateTaskDraft(): TaskCreateDraft
    suspend fun saveTaskDraftTitle(title: String)

    suspend fun saveTaskDraftDescription(description: String)

    suspend fun saveTaskDraftReceivers(receivers: List<String>)

    suspend fun saveTaskDraftAttachments(attachments: List<Attachment>)

    suspend fun saveTaskGradeRange(gradeRange: GradeRange)

    suspend fun saveTaskVariantDistributionMode(mode: VariantDistributionMode)

    suspend fun saveVariants(variants: List<TaskVariant>)

    suspend fun clearNotificationDraft()
}