package org.example.project.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDataStore(): DataStore<Preferences> {
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

    val prefsFile = File(appDataDir, DATA_STORE_FILE_NAME)

    return createDataStore {
        prefsFile.absolutePath
    }
}