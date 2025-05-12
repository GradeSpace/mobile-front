package org.example.project.features.lessons.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface LessonRepository {
    fun fetchLessonEvents(): Flow<LessonBlocks>

    fun getLesson(eventId: String?): Flow<LessonEventItem?>

    suspend fun actualizeLessons(): EmptyResult<DataError.Remote>

    suspend fun actualizeLesson(eventId: String?): EmptyResult<DataError.Remote>

    suspend fun createLesson(event: LessonEventItem): EmptyResult<DataError>
}