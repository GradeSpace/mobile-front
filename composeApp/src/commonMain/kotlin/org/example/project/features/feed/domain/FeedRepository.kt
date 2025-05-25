package org.example.project.features.feed.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface FeedRepository {
    fun fetchReceivers(): Flow<List<String>>

    fun fetchFeedEvents(): Flow<List<FeedEventItem>>

    fun getEvent(eventId: String?): Flow<FeedEventItem?>

    suspend fun getCurrentUser(): User?

    suspend fun actualizeEvents(): EmptyResult<DataError.Remote>

    suspend fun actualizeEvent(notificationId: String?): EmptyResult<DataError.Remote>

    suspend fun createEvent(event: FeedEventItem): EmptyResult<DataError>

    suspend fun getNotificationDraftTitle(): String
    suspend fun saveNotificationDraftTitle(title: String)

    suspend fun getNotificationDraftDescription(): String
    suspend fun saveNotificationDraftDescription(description: String)

    suspend fun getNotificationDraftReceivers(): List<String>
    suspend fun saveNotificationDraftReceivers(receivers: List<String>)

    suspend fun getNotificationDraftAttachments(): List<Attachment>
    suspend fun saveNotificationDraftAttachments(attachments: List<Attachment>)

    suspend fun clearNotificationDraft()
}
