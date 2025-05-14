package org.example.project.core.data.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.core.data.model.Language
import org.example.project.core.data.model.Theme

fun DataStorePreferences.getTheme(): Flow<Theme> {
    return this.getIntFlow(THEME_KEY, Theme.SYSTEM.id).map { themeId ->
        Theme.entries.firstOrNull { theme ->
            themeId == theme.id
        } ?: Theme.SYSTEM
    }
}

suspend fun DataStorePreferences.setTheme(theme: Theme) {
    return this.setInt(THEME_KEY, theme.id)
}

fun DataStorePreferences.getLanguage(): Flow<Language> {
    return this.getIntFlow(LANGUAGE_KEY, Language.RUSSIAN.id).map { languageId ->
        Language.entries.firstOrNull { language ->
            languageId == language.id
        } ?: Language.RUSSIAN
    }
}

suspend fun DataStorePreferences.setLanguage(language: Language) {
    return this.setInt(LANGUAGE_KEY, language.id)
}

private const val THEME_KEY = "theme_key"
private const val LANGUAGE_KEY = "language_key"

