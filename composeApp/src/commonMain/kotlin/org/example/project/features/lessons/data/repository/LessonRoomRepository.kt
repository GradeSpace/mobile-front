package org.example.project.features.lessons.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.repository.UserRepository
import org.example.project.features.lessons.data.database.clearLessonDraft
import org.example.project.features.lessons.data.database.dao.LessonsDao
import org.example.project.features.lessons.data.database.getLessonCreateDraft
import org.example.project.features.lessons.data.database.getLessonDraftAttachments
import org.example.project.features.lessons.data.database.getLessonDraftDate
import org.example.project.features.lessons.data.database.getLessonDraftDescription
import org.example.project.features.lessons.data.database.getLessonDraftEndTime
import org.example.project.features.lessons.data.database.getLessonDraftOfflinePlace
import org.example.project.features.lessons.data.database.getLessonDraftOnlineLink
import org.example.project.features.lessons.data.database.getLessonDraftReceivers
import org.example.project.features.lessons.data.database.getLessonDraftStartTime
import org.example.project.features.lessons.data.database.getLessonDraftSubject
import org.example.project.features.lessons.data.database.getLessonDraftTitle
import org.example.project.features.lessons.data.database.isLessonDraftOfflineLocationEnabled
import org.example.project.features.lessons.data.database.isLessonDraftOnlineLocationEnabled
import org.example.project.features.lessons.data.database.saveLessonDraftAttachments
import org.example.project.features.lessons.data.database.saveLessonDraftDate
import org.example.project.features.lessons.data.database.saveLessonDraftDescription
import org.example.project.features.lessons.data.database.saveLessonDraftEndTime
import org.example.project.features.lessons.data.database.saveLessonDraftOfflineLocationEnabled
import org.example.project.features.lessons.data.database.saveLessonDraftOfflinePlace
import org.example.project.features.lessons.data.database.saveLessonDraftOnlineLink
import org.example.project.features.lessons.data.database.saveLessonDraftOnlineLocationEnabled
import org.example.project.features.lessons.data.database.saveLessonDraftReceivers
import org.example.project.features.lessons.data.database.saveLessonDraftStartTime
import org.example.project.features.lessons.data.database.saveLessonDraftSubject
import org.example.project.features.lessons.data.database.saveLessonDraftTitle
import org.example.project.features.lessons.data.mappers.toDomain
import org.example.project.features.lessons.data.mappers.toEntity
import org.example.project.features.lessons.data.mappers.toLessonEntity
import org.example.project.features.lessons.domain.LessonCreateDraft
import org.example.project.features.lessons.domain.LessonEventItem
import org.example.project.features.lessons.domain.LessonRepository

class LessonRoomRepository(
    private val lessonsDao: LessonsDao,
    private val dataStorePreferences: DataStorePreferences,
    private val userRepository: UserRepository
) : LessonRepository {

    override fun fetchLessonEvents(): Flow<Map<LocalDate, List<LessonEventItem>>> {
        return lessonsDao.getAllLessonsWithRelations().map { lessonsWithRelations ->
            // Преобразуем сущности в доменные модели
            val domainLessons = lessonsWithRelations.map { lessonWithRelations ->
                lessonWithRelations.lesson.toDomain(
                    attachments = lessonWithRelations.attachments
                )
            }

            // Группируем занятия по дате
            domainLessons.groupBy { it.startTime.date }
        }
    }

    override fun getLesson(eventId: String?): Flow<LessonEventItem?> {
        if (eventId == null) return kotlinx.coroutines.flow.flowOf(null)

        return lessonsDao.getLessonWithRelations(eventId).map { lessonWithRelations ->
            lessonWithRelations?.let {
                it.lesson.toDomain(
                    attachments = it.attachments
                )
            }
        }
    }

    override suspend fun actualizeLessons(): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API
        return Result.Success(Unit)
    }

    override suspend fun actualizeLesson(eventId: String?): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API для конкретного занятия
        return Result.Success(Unit)
    }

    override suspend fun createLesson(event: LessonEventItem): EmptyResult<DataError> {
        try {
            val lessonEntity = event.toEntity()

            val attachmentEntities = event.attachments.mapNotNull {
                it.toLessonEntity(event.id)
            }

            lessonsDao.insertLessonWithRelations(
                lesson = lessonEntity,
                attachments = attachmentEntities
            )

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun fetchReceivers(): Flow<List<String>> {
        return flow {
            emit(
                listOf("Group 1", "Group 2", "Group 3", "Group 4", "Group 5")
            )
        }
    }

    // Методы для работы с черновиком занятия
    override suspend fun getLessonDraftTitle(): String? {
        return dataStorePreferences.getLessonDraftTitle()
    }

    override suspend fun saveLessonDraftTitle(title: String) {
        dataStorePreferences.saveLessonDraftTitle(title)
    }

    override suspend fun getLessonDraftSubject(): String? {
        return dataStorePreferences.getLessonDraftSubject()
    }

    override suspend fun saveLessonDraftSubject(subject: String) {
        dataStorePreferences.saveLessonDraftSubject(subject)
    }

    override suspend fun getLessonDraftDescription(): String? {
        return dataStorePreferences.getLessonDraftDescription()
    }

    override suspend fun saveLessonDraftDescription(description: String) {
        dataStorePreferences.saveLessonDraftDescription(description)
    }

    override suspend fun getLessonDraftReceivers(): List<String>? {
        return dataStorePreferences.getLessonDraftReceivers()
    }

    override suspend fun saveLessonDraftReceivers(receivers: List<String>) {
        dataStorePreferences.saveLessonDraftReceivers(receivers)
    }

    override suspend fun getLessonDraftAttachments(): List<Attachment>? {
        return dataStorePreferences.getLessonDraftAttachments()
    }

    override suspend fun saveLessonDraftAttachments(attachments: List<Attachment>) {
        dataStorePreferences.saveLessonDraftAttachments(attachments)
    }

    override suspend fun getLessonDraftDate(): LocalDate? {
        return dataStorePreferences.getLessonDraftDate()
    }

    override suspend fun saveLessonDraftDate(date: LocalDate?) {
        dataStorePreferences.saveLessonDraftDate(date)
    }

    override suspend fun getLessonDraftStartTime(): LocalDateTime? {
        return dataStorePreferences.getLessonDraftStartTime()
    }

    override suspend fun saveLessonDraftStartTime(time: LocalDateTime?) {
        dataStorePreferences.saveLessonDraftStartTime(time)
    }

    override suspend fun getLessonDraftEndTime(): LocalDateTime? {
        return dataStorePreferences.getLessonDraftEndTime()
    }

    override suspend fun saveLessonDraftEndTime(time: LocalDateTime?) {
        dataStorePreferences.saveLessonDraftEndTime(time)
    }

    override suspend fun isLessonDraftOnlineLocationEnabled(): Boolean {
        return dataStorePreferences.isLessonDraftOnlineLocationEnabled()
    }

    override suspend fun saveLessonDraftOnlineLocationEnabled(enabled: Boolean) {
        dataStorePreferences.saveLessonDraftOnlineLocationEnabled(enabled)
    }

    override suspend fun isLessonDraftOfflineLocationEnabled(): Boolean {
        return dataStorePreferences.isLessonDraftOfflineLocationEnabled()
    }

    override suspend fun saveLessonDraftOfflineLocationEnabled(enabled: Boolean) {
        dataStorePreferences.saveLessonDraftOfflineLocationEnabled(enabled)
    }

    override suspend fun getLessonDraftOnlineLink(): String? {
        return dataStorePreferences.getLessonDraftOnlineLink()
    }

    override suspend fun saveLessonDraftOnlineLink(link: String) {
        dataStorePreferences.saveLessonDraftOnlineLink(link)
    }

    override suspend fun getLessonDraftOfflinePlace(): String? {
        return dataStorePreferences.getLessonDraftOfflinePlace()
    }

    override suspend fun saveLessonDraftOfflinePlace(place: String) {
        dataStorePreferences.saveLessonDraftOfflinePlace(place)
    }

    override suspend fun getLessonCreateDraft(): LessonCreateDraft {
        return dataStorePreferences.getLessonCreateDraft()
    }

    override suspend fun currentUser(): User? {
        return userRepository.getCurrentUser()
    }

    override suspend fun clearLessonDraft() {
        dataStorePreferences.clearLessonDraft()
    }
}
