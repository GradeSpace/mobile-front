package org.example.project.features.feed.data.database.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.example.project.features.feed.data.database.entities.AttachmentEntity
import org.example.project.features.feed.data.database.entities.FeedActionEntity
import org.example.project.features.feed.data.database.entities.FeedEventEntity

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: FeedEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<FeedEventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: AttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: FeedActionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActions(actions: List<FeedActionEntity>)

    @Query("SELECT * FROM feed_events ORDER BY lastUpdateDateTime DESC")
    fun getAllEvents(): Flow<List<FeedEventEntity>>

    @Query("SELECT * FROM feed_events WHERE id = :eventId")
    fun getEventById(eventId: String): Flow<FeedEventEntity?>

    @Query("SELECT * FROM feed_attachments WHERE eventId = :eventId")
    fun getAttachmentsForEvent(eventId: String): Flow<List<AttachmentEntity>>

    @Query("SELECT * FROM feed_actions WHERE eventId = :eventId")
    fun getActionsForEvent(eventId: String): Flow<List<FeedActionEntity>>

    @Query("DELETE FROM feed_events")
    suspend fun clearAllEvents()

    @Transaction
    @Query("SELECT * FROM feed_events ORDER BY lastUpdateDateTime DESC")
    fun getAllEventsWithRelations(): Flow<List<EventWithRelations>>

    data class EventWithRelations(
        @Embedded val event: FeedEventEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "eventId"
        )
        val attachments: List<AttachmentEntity>,
        @Relation(
            parentColumn = "id",
            entityColumn = "eventId"
        )
        val actions: List<FeedActionEntity>
    )

    @Transaction
    suspend fun insertEventWithRelations(
        event: FeedEventEntity,
        attachments: List<AttachmentEntity>,
        actions: List<FeedActionEntity>
    ) {
        insertEvent(event)
        if (attachments.isNotEmpty()) {
            insertAttachments(attachments)
        }
        if (actions.isNotEmpty()) {
            insertActions(actions)
        }
    }
}
