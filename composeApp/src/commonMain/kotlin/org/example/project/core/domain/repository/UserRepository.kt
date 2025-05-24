package org.example.project.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.data.model.user.User
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult

interface UserRepository {
    // User data
    suspend fun getCurrentUser(): User?
    fun getCurrentUserAsFlow(): Flow<User?>
    suspend fun saveUser(user: User): EmptyResult<DataError.Local>

    // User profile data
    suspend fun getUserEmail(): String
    suspend fun saveUserEmail(email: String)
    suspend fun getUserPhone(): String
    suspend fun saveUserPhone(phone: String?)

    // User details
    suspend fun getUserFirstName(): String
    suspend fun saveUserFirstName(firstName: String)
    suspend fun getUserLastName(): String
    suspend fun saveUserLastName(lastName: String)
    suspend fun getUserMiddleName(): String
    suspend fun saveUserMiddleName(middleName: String?)
    suspend fun getUserRole(): UserRole
    suspend fun saveUserRole(role: UserRole)
    suspend fun getUserGroup(): String
    suspend fun saveUserGroup(group: String?)

    suspend fun isAuthenticated(): Boolean

    suspend fun saveUserAuthStatus(isAuthenticated: Boolean): EmptyResult<DataError.Local>

    fun isAuthenticatedAsFlow(): Flow<Boolean?>

    // Clear user data
    suspend fun clearUserData(): EmptyResult<DataError.Local>
}
