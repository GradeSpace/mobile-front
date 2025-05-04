package org.example.project.core.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<GradeSpaceDatabase>
}