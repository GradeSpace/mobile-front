package org.example.project.features.profile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.datastore.getLanguage
import org.example.project.core.data.datastore.getTheme
import org.example.project.core.data.datastore.setLanguage
import org.example.project.core.data.datastore.setTheme
import org.example.project.core.data.model.Language
import org.example.project.core.data.model.Theme
import org.example.project.core.data.model.user.User
import org.example.project.core.data.model.user.UserStudent
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.features.profile.domain.ProfileItem
import org.example.project.features.profile.domain.ProfileRepository

class ProfileRepositoryImpl(
    val dataStorePreferences: DataStorePreferences
) : ProfileRepository {
    override fun getUserInfo(): Flow<User> {
        return flow {
            emit(
                UserStudent(
                    name = "Test_Name",
                    surname = "Test_Surname",
                    middleName = "Test_MiddleName",
                    group = "ИУ9-62Б"
                )
            )
        }
    }

    override fun getProfileItems(): Flow<List<ProfileItem>> {
        return dataStorePreferences
            .getTheme()
            .combine(dataStorePreferences.getLanguage()) { theme, language ->
                val items = mutableListOf<ProfileItem>()

                items.add(
                    ProfileItem.PERSONAL_DATA
                )

                items.add(
                    when (language) {
                        Language.RUSSIAN -> ProfileItem.RUSSIAN_LANGUAGE
                        Language.ENGLISH -> ProfileItem.ENGLISH_LANGUAGE
                    }
                )

                items.add(
                    when (theme) {
                        Theme.LIGHT -> ProfileItem.LIGHT_THEME
                        Theme.DARK -> ProfileItem.DARK_THEME
                        Theme.SYSTEM -> ProfileItem.SYSTEM_THEME
                    }
                )

                items.add(
                    ProfileItem.EXIT
                )

                items
            }
    }

    override suspend fun getLanguages(): Flow<List<Pair<Language, Boolean>>> {
        return dataStorePreferences.getLanguage().map { selectedLanguage ->
            Language.entries.map { language ->
                Pair(language, language == selectedLanguage)
            }
        }
    }

    override suspend fun getThemes(): Flow<List<Pair<Theme, Boolean>>> {
        return dataStorePreferences.getTheme().map { currentTheme ->
            Theme.entries.map { theme ->
                Pair(theme, theme == currentTheme)
            }
        }
    }

    override suspend fun changeTheme(theme: Theme) {
        dataStorePreferences.setTheme(theme)
    }

    override suspend fun changeLanguage(language: Language) {
        dataStorePreferences.setLanguage(language)
    }

    override suspend fun actualize(): EmptyResult<DataError.Remote> {
        return Result.Success(Unit)
    }
}
