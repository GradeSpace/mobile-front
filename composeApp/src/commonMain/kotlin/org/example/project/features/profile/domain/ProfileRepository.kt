package org.example.project.features.profile.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.core.data.model.Language
import org.example.project.core.data.model.Theme
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface ProfileRepository {
    fun getUserInfo(): Flow<User>
    fun getProfileItems(): Flow<List<ProfileItem>>
    suspend fun getLanguages(): Flow<List<Pair<Language, Boolean>>>
    suspend fun getThemes(): Flow<List<Pair<Theme, Boolean>>>
    suspend fun changeTheme(theme: Theme)
    suspend fun changeLanguage(language: Language)
    suspend fun actualize(): EmptyResult<DataError.Remote>
}