package org.example.project.features.feed.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.repository.UserRepository
import org.example.project.features.feed.data.database.clearNotificationDraft
import org.example.project.features.feed.data.database.dao.FeedDao
import org.example.project.features.feed.data.database.getNotificationDraftAttachments
import org.example.project.features.feed.data.database.getNotificationDraftDescription
import org.example.project.features.feed.data.database.getNotificationDraftReceivers
import org.example.project.features.feed.data.database.getNotificationDraftTitle
import org.example.project.features.feed.data.database.saveNotificationDraftAttachments
import org.example.project.features.feed.data.database.saveNotificationDraftDescription
import org.example.project.features.feed.data.database.saveNotificationDraftReceivers
import org.example.project.features.feed.data.database.saveNotificationDraftTitle
import org.example.project.features.feed.data.mappers.toDomain
import org.example.project.features.feed.data.mappers.toEntity
import org.example.project.features.feed.domain.FeedEventItem
import org.example.project.features.feed.domain.FeedRepository

class FeedRoomRepository(
    private val feedDao: FeedDao,
    private val dataStorePreferences: DataStorePreferences,
    private val userRepository: UserRepository
) : FeedRepository {

    override suspend fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    override fun fetchReceivers(): Flow<List<String>> {
        return flow {
            emit(listOf("Group 1", "Group 2", "Group 3", "Group 4", "Group 5", "Group 6"))
        }
    }

    override fun fetchFeedEvents(): Flow<List<FeedEventItem>> {
        return feedDao.getAllEventsWithRelations().map { eventsWithRelations ->
            eventsWithRelations.map { eventWithRelations ->
                eventWithRelations.event.toDomain(
                    attachments = eventWithRelations.attachments,
                    actions = eventWithRelations.actions
                )
            }
        }
    }


    override fun getEvent(eventId: String?): Flow<FeedEventItem?> {
        if (eventId == null) return kotlinx.coroutines.flow.flowOf(null)

        return combine(
            feedDao.getEventById(eventId),
            feedDao.getAttachmentsForEvent(eventId),
            feedDao.getActionsForEvent(eventId)
        ) { eventEntity, attachments, actions ->
            eventEntity?.toDomain(attachments, actions)
        }
    }

    override suspend fun actualizeEvents(): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API
        return Result.Success(Unit)
    }

    override suspend fun actualizeEvent(notificationId: String?): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API для конкретного события
        return Result.Success(Unit)
    }

    override suspend fun createEvent(event: FeedEventItem): EmptyResult<DataError> {
        try {
            val eventEntity = event.toEntity()

            val attachmentEntities = event.attachments.mapNotNull {
                it.toEntity(event.id)
            }

            val actionEntities = event.actions.map {
                it.toEntity(event.id)
            }

            feedDao.insertEventWithRelations(
                event = eventEntity,
                attachments = attachmentEntities,
                actions = actionEntities
            )

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    // Методы для работы с черновиком уведомления
    override suspend fun getNotificationDraftTitle(): String {
        return dataStorePreferences.getNotificationDraftTitle()
    }

    override suspend fun saveNotificationDraftTitle(title: String) {
        dataStorePreferences.saveNotificationDraftTitle(title)
    }

    override suspend fun getNotificationDraftDescription(): String {
        return dataStorePreferences.getNotificationDraftDescription()
    }

    override suspend fun saveNotificationDraftDescription(description: String) {
        dataStorePreferences.saveNotificationDraftDescription(description)
    }

    override suspend fun getNotificationDraftReceivers(): List<String> {
        return dataStorePreferences.getNotificationDraftReceivers()
    }

    override suspend fun saveNotificationDraftReceivers(receivers: List<String>) {
        dataStorePreferences.saveNotificationDraftReceivers(receivers)
    }

    override suspend fun getNotificationDraftAttachments(): List<Attachment> {
        return dataStorePreferences.getNotificationDraftAttachments()
    }

    override suspend fun saveNotificationDraftAttachments(attachments: List<Attachment>) {
        dataStorePreferences.saveNotificationDraftAttachments(attachments)
    }

    override suspend fun clearNotificationDraft() {
        dataStorePreferences.clearNotificationDraft()
    }
}
