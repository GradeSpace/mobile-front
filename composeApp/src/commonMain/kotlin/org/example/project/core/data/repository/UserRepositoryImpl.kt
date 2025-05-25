package org.example.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.datastore.clearUserData
import org.example.project.core.data.datastore.getUser
import org.example.project.core.data.datastore.getUserAuthenticationStatusFlow
import org.example.project.core.data.datastore.getUserEmail
import org.example.project.core.data.datastore.getUserFirstName
import org.example.project.core.data.datastore.getUserFlow
import org.example.project.core.data.datastore.getUserGroup
import org.example.project.core.data.datastore.getUserLastName
import org.example.project.core.data.datastore.getUserMiddleName
import org.example.project.core.data.datastore.getUserPhone
import org.example.project.core.data.datastore.getUserRole
import org.example.project.core.data.datastore.isUserAuthenticated
import org.example.project.core.data.datastore.saveUser
import org.example.project.core.data.datastore.saveUserAuthenticationStatus
import org.example.project.core.data.datastore.saveUserEmail
import org.example.project.core.data.datastore.saveUserFirstName
import org.example.project.core.data.datastore.saveUserGroup
import org.example.project.core.data.datastore.saveUserLastName
import org.example.project.core.data.datastore.saveUserMiddleName
import org.example.project.core.data.datastore.saveUserPhone
import org.example.project.core.data.datastore.saveUserRole
import org.example.project.core.data.model.user.User
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataStore: DataStorePreferences
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        return dataStore.getUser()
    }

    override fun getCurrentUserAsFlow(): Flow<User?> {
        return dataStore.getUserFlow()
    }

    override suspend fun saveUser(user: User): EmptyResult<DataError.Local> {
        dataStore.saveUser(user)
        return Result.Success(Unit)
    }

    override suspend fun getUserEmail(): String {
        return dataStore.getUserEmail()
    }

    override suspend fun saveUserEmail(email: String) {
        dataStore.saveUserEmail(email)
    }

    override suspend fun getUserPhone(): String {
        return dataStore.getUserPhone()
    }

    override suspend fun saveUserPhone(phone: String?) {
        dataStore.saveUserPhone(phone)
    }

    override suspend fun getUserFirstName(): String {
        return dataStore.getUserFirstName()
    }

    override suspend fun saveUserFirstName(firstName: String) {
        dataStore.saveUserFirstName(firstName)
    }

    override suspend fun getUserLastName(): String {
        return dataStore.getUserLastName()
    }

    override suspend fun saveUserLastName(lastName: String) {
        dataStore.saveUserLastName(lastName)
    }

    override suspend fun getUserMiddleName(): String {
        return dataStore.getUserMiddleName()
    }

    override suspend fun saveUserMiddleName(middleName: String?) {
        dataStore.saveUserMiddleName(middleName)
    }

    override suspend fun getUserRole(): UserRole {
        return dataStore.getUserRole()
    }

    override suspend fun saveUserRole(role: UserRole) {
        dataStore.saveUserRole(role)
    }

    override suspend fun getUserGroup(): String {
        return dataStore.getUserGroup()
    }

    override suspend fun saveUserGroup(group: String?) {
        dataStore.saveUserGroup(group)
    }

    override suspend fun clearUserData(): EmptyResult<DataError.Local> {
        dataStore.clearUserData()
        return Result.Success(Unit)
    }

    override suspend fun isAuthenticated(): Boolean {
        return dataStore.isUserAuthenticated()
    }

    override suspend fun saveUserAuthStatus(isAuthenticated: Boolean): EmptyResult<DataError.Local> {
        dataStore.saveUserAuthenticationStatus(isAuthenticated)
        return Result.Success(Unit)
    }

    override fun isAuthenticatedAsFlow(): Flow<Boolean?> {
        return dataStore.getUserAuthenticationStatusFlow()
    }
}
