package org.example.project.features.lessons.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.presentation.UiText
import org.example.project.features.feed.data.mock.FeedTextMock
import org.example.project.features.lessons.data.database.clearLessonDraft
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
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonBlocks
import org.example.project.features.lessons.domain.LessonCreateDraft
import org.example.project.features.lessons.domain.LessonEventItem
import org.example.project.features.lessons.domain.LessonRepository
import org.example.project.features.lessons.domain.LessonStatus
import kotlin.random.Random

class LessonMockRepository(
    private val dataStorePreferences: DataStorePreferences
) : LessonRepository {

    private val localLessons = mutableListOf<LessonEventItem>()
    val localLessonBlocks = mutableMapOf<LocalDate, List<LessonEventItem>>()

    init {
        initLessons()
    }

    private fun initLessons() {
        val mockTeacher = User(
            uid = "teacher1",
            name = "Иван",
            surname = "Иванов",
            middleName = "Иванович"
        )

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Generate lessons for past, current, and future days
        for (dayOffset in -14..14) {
            val lessonDate = currentDateTime.date.plus(dayOffset, DateTimeUnit.DAY)

            // Generate 2-5 lessons per day
            val lessonsCount = Random.nextInt(2, 6)

            for (lessonIndex in 0 until lessonsCount) {
                // Create lesson start time between 8:00 and 19:00
                val startHour = Random.nextInt(8, 22)
                val startMinute = Random.nextInt(0, 12) * 5 // 5-minute intervals

                val startTime = LocalTime(
                    hour = startHour,
                    minute = startMinute
                )

                // Lesson duration between 1 and 3 hours
                val durationHours = Random.nextInt(1, 4)
                val endTime = LocalTime(
                    hour = (startHour + durationHours).coerceAtMost(21),
                    minute = startMinute
                )

                val lessonStartDateTime = LocalDateTime(lessonDate, startTime)
                val lessonEndDateTime = LocalDateTime(lessonDate, endTime)

                // Determine lesson status based on current time
                val lessonStatus = when {
                    lessonEndDateTime < currentDateTime -> LessonStatus.Finished
                    lessonStartDateTime <= currentDateTime && lessonEndDateTime >= currentDateTime -> LessonStatus.InProgress
                    else -> LessonStatus.NotStarted
                }

                // Determine attendance status
                val attendanceStatus = when {
                    lessonStatus == LessonStatus.Finished -> {
                        when (Random.nextInt(0, 3)) {
                            0 -> AttendanceStatus.NotAttended
                            1 -> AttendanceStatus.AttendanceOnCheck
                            else -> AttendanceStatus.Attended
                        }
                    }
                    lessonStatus == LessonStatus.InProgress -> {
                        when (Random.nextInt(0, 2)) {
                            0 -> AttendanceStatus.NotAttended
                            else -> AttendanceStatus.AttendanceOnCheck
                        }
                    }
                    else -> AttendanceStatus.NotAttended
                }

                // Determine if lesson is online or offline
                val isOnline = Random.nextBoolean()
                val location = if (isOnline) {
                    EventLocation(
                        lessonUrl = "https://meet.example.com/lesson-${lessonDate}-${lessonIndex}"
                    )
                } else {
                    EventLocation(
                        cabinet = "${Random.nextInt(100, 500)}"
                    )
                }

                // Create subject names
                val subjects = listOf(
                    "Математический анализ",
                    "Линейная алгебра",
                    "Программирование",
                    "Физика",
                    "Информатика",
                    "Базы данных",
                    "Компьютерные сети",
                    "Операционные системы",
                    "Алгоритмы и структуры данных",
                    "Машинное обучение"
                )

                val lesson = LessonEventItem(
                    id = "lesson_${lessonDate}_$lessonIndex",
                    title = subjects[Random.nextInt(subjects.size)],
                    description = if (Random.nextBoolean()) {
                        UiText.DynamicString(FeedTextMock.getRandomDescription())
                    } else {
                        UiText.StringResourceId(Res.string.no_description)
                    },
                    author = mockTeacher,
                    lastUpdateDateTime = lessonStartDateTime,
                    attachments = if (Random.nextBoolean()) {
                        (0..Random.nextInt(0, 3)).map { index ->
                            val fileType = Attachment.FileType.entries.random()
                            Attachment.FileAttachment(
                                url = "https://example.com/lesson_${lessonDate}_${lessonIndex}_attachment_$index",
                                fileName = "Материал ${index + 1}.$fileType",
                                fileSize = Random.nextLong(100000, 5000000),
                                fileType = fileType,
                                id = index.toString()
                            )
                        }
                    } else {
                        emptyList()
                    },
                    receivers = listOf("ИУ9-62Б", "ИУ9-61Б"),
                    subject = subjects[Random.nextInt(subjects.size)],
                    location = location,
                    startTime = lessonStartDateTime,
                    endTime = lessonEndDateTime,
                    attendanceStatus = attendanceStatus,
                    lessonStatus = lessonStatus
                )

                localLessons.add(lesson)
            }
        }

        updateLessonBlocks()
    }

    private fun updateLessonBlocks() {
        // Group lessons by date
        val groupedLessons = localLessons.groupBy { it.startTime.date }

        // Sort lessons within each day by start time
        localLessonBlocks.clear()
        groupedLessons.forEach { (date, lessons) ->
            localLessonBlocks[date] = lessons.sortedBy { it.startTime }
        }
    }

    override fun fetchLessonEvents(): Flow<LessonBlocks> {
    return flow {
        emit(localLessonBlocks)
    }
}


    override fun getLesson(eventId: String?): Flow<LessonEventItem?> {
        return flow {
            emit(localLessons.firstOrNull { it.id == eventId })
        }
    }

    override suspend fun actualizeLessons(): EmptyResult<DataError.Remote> {
        localLessons.clear()
        localLessonBlocks.clear()
        initLessons()
        return Result.Success(Unit)
    }

    override suspend fun actualizeLesson(eventId: String?): EmptyResult<DataError.Remote> {
        return Result.Success(Unit)
    }

    override suspend fun createLesson(event: LessonEventItem): EmptyResult<DataError> {
        localLessons.add(event)
        updateLessonBlocks()
        return Result.Success(Unit)
    }

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

    override suspend fun clearLessonDraft() {
        dataStorePreferences.clearLessonDraft()
    }

    override fun fetchReceivers(): Flow<List<String>> {
        return flow {
            emit(listOf(
                "ИУ9-62Б", "ИУ9-61Б", "ИУ9-11М", "ИУ9-12М", "ИУ9-31Б", "ИУ9-32Б"
            ))
        }
    }
}
