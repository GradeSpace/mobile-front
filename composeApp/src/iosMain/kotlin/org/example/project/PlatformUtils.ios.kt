package org.example.project

import org.example.project.core.data.model.Language
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getSystemLanguage(): Language {
    val locale = NSLocale.currentLocale
    val languageCode = locale.languageCode

    return when (languageCode) {
        "ru" -> Language.RUSSIAN
        "en" -> Language.ENGLISH
        // Добавьте другие языки по необходимости
        else -> Language.ENGLISH // Язык по умолчанию, если системный язык не поддерживается
    }
}