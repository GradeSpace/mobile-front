package org.example.project.features.feed.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface FeedRepository {
    fun fetchFeedEvents(): Flow<List<FeedEventsBlock>>

    fun getEvent(eventId: String?): Flow<FeedEventItem?>

    suspend fun actualizeEvents(): EmptyResult<DataError.Remote>

    suspend fun actualizeEvent(notificationId: String?): EmptyResult<DataError.Remote>

    suspend fun createEvent(event: FeedEventItem): EmptyResult<DataError>
}