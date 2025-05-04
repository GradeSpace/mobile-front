package org.example.project.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.example.project.core.data.database.converters.StringListTypeConverter
import org.example.project.core.data.database.dao.CalendarDao
import org.example.project.features.feed.data.database.dao.FeedDao
import org.example.project.core.data.database.dao.ProfileDao
import org.example.project.core.data.database.dao.TasksDao
import org.example.project.features.feed.data.database.entities.FeedEventEntity

@Database(
    entities = [
        // Лента
        FeedEventEntity::class
    ],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class GradeSpaceDatabase : RoomDatabase() {

    abstract val feedDao: FeedDao
    abstract val tasksDao: TasksDao
    abstract val calendarDao: CalendarDao
    abstract val profileDao: ProfileDao

    companion object {
        const val DB_NAME = "grade_space.db"
    }
}