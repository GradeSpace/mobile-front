package org.example.project.features.feed.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result

interface FeedRepository {
    fun fetchFeedEvents(): Flow<List<FeedEventsBlock>>

    suspend fun actualizeEvents(): EmptyResult<DataError.Remote>

    suspend fun getEvent(eventId: String): Flow<FeedEventItem?>

    suspend fun actualizeEvent(): EmptyResult<DataError.Remote>

    suspend fun createEvent(event: FeedEventItem): EmptyResult<DataError>
}