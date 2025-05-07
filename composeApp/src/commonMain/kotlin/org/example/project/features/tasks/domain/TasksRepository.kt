package org.example.project.features.tasks.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.features.tasks.domain.TasksEventsBlock.BlockType

interface TasksRepository {
    fun fetchTasksEvents(filter: Set<BlockType> = BlockType.entries.toSet()): Flow<List<TasksEventsBlock>>

    fun getTask(eventId: String?): Flow<TaskEventItem?>

    suspend fun actualizeTasks(): EmptyResult<DataError.Remote>

    suspend fun actualizeTask(eventId: String?): EmptyResult<DataError.Remote>

    suspend fun createTask(event: TaskEventItem): EmptyResult<DataError>
}