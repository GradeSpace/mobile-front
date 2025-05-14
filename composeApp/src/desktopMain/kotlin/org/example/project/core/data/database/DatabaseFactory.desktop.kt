package org.example.project.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<GradeSpaceDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "GradeSpace")
            os.contains("mac") -> File(userHome, "Library/Application Support/GradeSpace")
            else -> File(userHome, ".local/share/GradeSpace")
        }

        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, GradeSpaceDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}