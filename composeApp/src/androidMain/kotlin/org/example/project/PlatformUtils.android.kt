package org.example.project

import org.example.project.core.data.model.Language
import java.util.Locale

actual fun getSystemLanguage(): Language {
    val locale = Locale.getDefault()
    val languageCode = locale.language

    return when (languageCode) {
        "ru" -> Language.RUSSIAN
        "en" -> Language.ENGLISH
        else -> Language.ENGLISH
    }
}