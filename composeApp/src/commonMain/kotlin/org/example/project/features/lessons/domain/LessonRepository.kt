package org.example.project.features.lessons.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface LessonRepository {
    fun fetchLessonEvents(): Flow<LessonBlocks>

    fun getLesson(eventId: String?): Flow<LessonEventItem?>

    suspend fun actualizeLessons(): EmptyResult<DataError.Remote>

    suspend fun actualizeLesson(eventId: String?): EmptyResult<DataError.Remote>

    suspend fun createLesson(event: LessonEventItem): EmptyResult<DataError>

    fun fetchReceivers(): Flow<List<String>>

    suspend fun getLessonDraftTitle(): String?
    suspend fun saveLessonDraftTitle(title: String)

    suspend fun getLessonDraftSubject(): String?
    suspend fun saveLessonDraftSubject(subject: String)

    suspend fun getLessonDraftDescription(): String?
    suspend fun saveLessonDraftDescription(description: String)

    suspend fun getLessonDraftReceivers(): List<String>?
    suspend fun saveLessonDraftReceivers(receivers: List<String>)

    suspend fun getLessonDraftAttachments(): List<Attachment>?
    suspend fun saveLessonDraftAttachments(attachments: List<Attachment>)

    suspend fun getLessonDraftDate(): LocalDate?
    suspend fun saveLessonDraftDate(date: LocalDate?)

    suspend fun getLessonDraftStartTime(): LocalDateTime?
    suspend fun saveLessonDraftStartTime(time: LocalDateTime?)

    suspend fun getLessonDraftEndTime(): LocalDateTime?
    suspend fun saveLessonDraftEndTime(time: LocalDateTime?)

    suspend fun isLessonDraftOnlineLocationEnabled(): Boolean
    suspend fun saveLessonDraftOnlineLocationEnabled(enabled: Boolean)

    suspend fun isLessonDraftOfflineLocationEnabled(): Boolean
    suspend fun saveLessonDraftOfflineLocationEnabled(enabled: Boolean)

    suspend fun getLessonDraftOnlineLink(): String?
    suspend fun saveLessonDraftOnlineLink(link: String)

    suspend fun getLessonDraftOfflinePlace(): String?
    suspend fun saveLessonDraftOfflinePlace(place: String)

    suspend fun getLessonCreateDraft(): LessonCreateDraft

    suspend fun clearLessonDraft()
}