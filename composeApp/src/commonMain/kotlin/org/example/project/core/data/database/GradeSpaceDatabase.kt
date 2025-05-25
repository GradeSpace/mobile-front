package org.example.project.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.example.project.core.data.database.converters.LocalDateConverter
import org.example.project.core.data.database.converters.LocalDateTimeConverter
import org.example.project.core.data.database.converters.StringListTypeConverter
import org.example.project.core.data.database.dao.ProfileDao
import org.example.project.features.feed.data.database.dao.FeedDao
import org.example.project.features.feed.data.database.entities.AttachmentEntity
import org.example.project.features.feed.data.database.entities.FeedActionEntity
import org.example.project.features.feed.data.database.entities.FeedEventEntity
import org.example.project.features.lessons.data.database.dao.LessonsDao
import org.example.project.features.lessons.data.database.entities.LessonAttachmentEntity
import org.example.project.features.lessons.data.database.entities.LessonEventEntity
import org.example.project.features.tasks.data.database.dao.TasksDao
import org.example.project.features.tasks.data.database.entities.TaskAttachmentEntity
import org.example.project.features.tasks.data.database.entities.TaskEventEntity
import org.example.project.features.tasks.data.database.entities.TaskVariantEntity

@Database(
    entities = [
        FeedEventEntity::class,
        AttachmentEntity::class,
        FeedActionEntity::class,

        TaskEventEntity::class,
        TaskAttachmentEntity::class,
        TaskVariantEntity::class,

        LessonEventEntity::class,
        LessonAttachmentEntity::class
    ],
    version = 3
)
@TypeConverters(
    StringListTypeConverter::class,
    LocalDateTimeConverter::class,
    LocalDateConverter::class
)
@ConstructedBy(GradeSpaceDatabaseConstructor::class)
abstract class GradeSpaceDatabase : RoomDatabase() {

    abstract val feedDao: FeedDao
    abstract val tasksDao: TasksDao
    abstract val lessonsDao: LessonsDao
    abstract val profileDao: ProfileDao

    companion object {
        const val DB_NAME = "grade_space.db"
    }
}