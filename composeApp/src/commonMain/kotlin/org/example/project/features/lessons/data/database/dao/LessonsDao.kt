package org.example.project.features.lessons.data.database.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import org.example.project.features.lessons.data.database.entities.LessonAttachmentEntity
import org.example.project.features.lessons.data.database.entities.LessonEventEntity

@Dao
interface LessonsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: LessonAttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<LessonAttachmentEntity>)

    @Query("SELECT * FROM lesson_events ORDER BY startTime DESC")
    fun getAllLessons(): Flow<List<LessonEventEntity>>

    @Query("SELECT * FROM lesson_events WHERE id = :lessonId")
    fun getLessonById(lessonId: String): Flow<LessonEventEntity?>

    @Query("SELECT * FROM lesson_attachments WHERE lessonId = :lessonId")
    fun getAttachmentsForLesson(lessonId: String): Flow<List<LessonAttachmentEntity>>

    @Query("DELETE FROM lesson_events")
    suspend fun clearAllLessons()

    @Transaction
    @Query("SELECT * FROM lesson_events ORDER BY startTime DESC")
    fun getAllLessonsWithRelations(): Flow<List<LessonWithRelations>>

    @Transaction
    @Query("SELECT * FROM lesson_events WHERE id = :lessonId")
    fun getLessonWithRelations(lessonId: String): Flow<LessonWithRelations?>

    // Получение занятий по дате (для календаря)
    @Transaction
    @Query("SELECT * FROM lesson_events WHERE date(startTime) = date(:date) ORDER BY startTime ASC")
    fun getLessonsByDate(date: LocalDate): Flow<List<LessonWithRelations>>

    // Получение всех дат, на которые есть занятия
    @Query("SELECT DISTINCT date(startTime) FROM lesson_events")
    fun getAllLessonDates(): Flow<List<LocalDate>>

    data class LessonWithRelations(
        @Embedded val lesson: LessonEventEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "lessonId"
        )
        val attachments: List<LessonAttachmentEntity>
    )

    @Transaction
    suspend fun insertLessonWithRelations(
        lesson: LessonEventEntity,
        attachments: List<LessonAttachmentEntity>
    ) {
        insertLesson(lesson)
        if (attachments.isNotEmpty()) {
            insertAttachments(attachments)
        }
    }
}
