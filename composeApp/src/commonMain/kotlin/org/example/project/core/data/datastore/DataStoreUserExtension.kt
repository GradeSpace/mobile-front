package org.example.project.core.data.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.example.project.core.data.model.user.User
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.data.model.user.UserStudent
import org.example.project.core.data.model.user.UserTeacher

// Keys for user data
private const val USER_ID_KEY = "user_id_key"
private const val USER_EMAIL_KEY = "user_email_key"
private const val USER_PHONE_KEY = "user_phone_key"
private const val USER_FIRST_NAME_KEY = "user_first_name_key"
private const val USER_LAST_NAME_KEY = "user_last_name_key"
private const val USER_MIDDLE_NAME_KEY = "user_middle_name_key"
private const val USER_ROLE_KEY = "user_role_key"
private const val USER_GROUP_KEY = "user_group_key"
private const val USER_IMAGE_URL_KEY = "user_image_url_key"
private const val USER_IS_AUTHENTICATED_KEY = "user_is_authenticated_key"

// User ID
suspend fun DataStorePreferences.getUserId(): String? {
    return getString(USER_ID_KEY, "")
}

suspend fun DataStorePreferences.saveUserId(userId: String) {
    setString(USER_ID_KEY, userId)
}

fun DataStorePreferences.getUserIdFlow(): Flow<String?> {
    return getStringFlow(USER_ID_KEY, "")
}

// User Email
suspend fun DataStorePreferences.getUserEmail(): String {
    return getString(USER_EMAIL_KEY, "")
}

suspend fun DataStorePreferences.saveUserEmail(email: String) {
    setString(USER_EMAIL_KEY, email)
}

fun DataStorePreferences.getUserEmailFlow(): Flow<String> {
    return getStringFlow(USER_EMAIL_KEY, "")
}

// User Phone
suspend fun DataStorePreferences.getUserPhone(): String {
    return getString(USER_PHONE_KEY, "")
}

suspend fun DataStorePreferences.saveUserPhone(phone: String?) {
    if (phone != null) {
        setString(USER_PHONE_KEY, phone)
    } else {
        setString(USER_PHONE_KEY, "")
    }
}

fun DataStorePreferences.getUserPhoneFlow(): Flow<String> {
    return getStringFlow(USER_PHONE_KEY, "")
}

// User First Name
suspend fun DataStorePreferences.getUserFirstName(): String {
    return getString(USER_FIRST_NAME_KEY, "")
}

suspend fun DataStorePreferences.saveUserFirstName(firstName: String) {
    setString(USER_FIRST_NAME_KEY, firstName)
}

fun DataStorePreferences.getUserFirstNameFlow(): Flow<String> {
    return getStringFlow(USER_FIRST_NAME_KEY, "")
}

// User Last Name
suspend fun DataStorePreferences.getUserLastName(): String {
    return getString(USER_LAST_NAME_KEY, "")
}

suspend fun DataStorePreferences.saveUserLastName(lastName: String) {
    setString(USER_LAST_NAME_KEY, lastName)
}

fun DataStorePreferences.getUserLastNameFlow(): Flow<String> {
    return getStringFlow(USER_LAST_NAME_KEY, "")
}

// User Middle Name
suspend fun DataStorePreferences.getUserMiddleName(): String {
    return getString(USER_MIDDLE_NAME_KEY, "")
}

suspend fun DataStorePreferences.saveUserMiddleName(middleName: String?) {
    if (middleName != null) {
        setString(USER_MIDDLE_NAME_KEY, middleName)
    } else {
        setString(USER_MIDDLE_NAME_KEY, "")
    }
}

fun DataStorePreferences.getUserMiddleNameFlow(): Flow<String> {
    return getStringFlow(USER_MIDDLE_NAME_KEY, "")
}

// User Role
suspend fun DataStorePreferences.getUserRole(): UserRole {
    val roleIndex = getInt(USER_ROLE_KEY, UserRole.Undefined.index)
    return UserRole.fromIndex(roleIndex)
}

suspend fun DataStorePreferences.saveUserRole(role: UserRole) {
    setInt(USER_ROLE_KEY, role.index)
}

fun DataStorePreferences.getUserRoleFlow(): Flow<UserRole> {
    return getIntFlow(USER_ROLE_KEY, UserRole.Undefined.index).map { roleIndex ->
        UserRole.fromIndex(roleIndex)
    }
}

// User Group (for students)
suspend fun DataStorePreferences.getUserGroup(): String {
    return getString(USER_GROUP_KEY, "")
}

suspend fun DataStorePreferences.saveUserGroup(group: String?) {
    if (group != null) {
        setString(USER_GROUP_KEY, group)
    } else {
        setString(USER_GROUP_KEY, "")
    }
}

fun DataStorePreferences.getUserGroupFlow(): Flow<String> {
    return getStringFlow(USER_GROUP_KEY, "")
}

// User Image URL
suspend fun DataStorePreferences.getUserImageUrl(): String {
    return getString(USER_IMAGE_URL_KEY, "")
}

suspend fun DataStorePreferences.saveUserImageUrl(imageUrl: String?) {
    if (imageUrl != null) {
        setString(USER_IMAGE_URL_KEY, imageUrl)
    } else {
        setString(USER_IMAGE_URL_KEY, "")
    }
}

fun DataStorePreferences.getUserImageUrlFlow(): Flow<String> {
    return getStringFlow(USER_IMAGE_URL_KEY, "")
}

// User Authentication Status
suspend fun DataStorePreferences.isUserAuthenticated(): Boolean {
    return getBoolean(USER_IS_AUTHENTICATED_KEY, false)
}

suspend fun DataStorePreferences.saveUserAuthenticationStatus(isAuthenticated: Boolean) {
    setBoolean(USER_IS_AUTHENTICATED_KEY, isAuthenticated)
}

fun DataStorePreferences.getUserAuthenticationStatusFlow(): Flow<Boolean?> {
    return getBooleanFlow(USER_IS_AUTHENTICATED_KEY, null)
}

// Get complete User object
suspend fun DataStorePreferences.getUser(): User? {
    val userId = getUserId() ?: return null
    val firstName = getUserFirstName()
    val lastName = getUserLastName()

    // Если имя или фамилия пустые, возвращаем null
    if (firstName.isEmpty() || lastName.isEmpty()) return null

    val middleName = getUserMiddleName().takeIf { it.isNotEmpty() }
    val imageUrl = getUserImageUrl().takeIf { it.isNotEmpty() }
    val role = getUserRole()

    return when (role) {
        UserRole.Student -> {
            val group = getUserGroup()
            if (group.isEmpty()) return null
            UserStudent(
                name = firstName,
                surname = lastName,
                middleName = middleName,
                imageUrl = imageUrl,
                uid = userId,
                group = group
            )
        }
        UserRole.Teacher -> {
            UserTeacher(
                name = firstName,
                surname = lastName,
                middleName = middleName,
                imageUrl = imageUrl,
                uid = userId
            )
        }
        UserRole.Undefined -> {
            User(
                name = firstName,
                surname = lastName,
                middleName = middleName,
                imageUrl = imageUrl,
                uid = userId,
                role = role
            )
        }
    }
}

fun DataStorePreferences.getUserFlow(): Flow<User?> {
    return combine(
        getUserIdFlow(),
        getUserFirstNameFlow(),
        getUserLastNameFlow(),
        getUserMiddleNameFlow(),
        getUserImageUrlFlow(),
        getUserRoleFlow(),
        getUserGroupFlow()
    ) { array ->
        val userId = array[0] as String?
        val firstName = array[1] as String
        val lastName = array[2] as String
        val middleName = array[3] as String
        val imageUrl = array[4] as String
        val role = array[5] as UserRole
        val group = array[6] as String

        if (userId == null || firstName.isEmpty() || lastName.isEmpty()) {
            return@combine null
        }

        val nonEmptyMiddleName = middleName.takeIf { it.isNotEmpty() }
        val nonEmptyImageUrl = imageUrl.takeIf { it.isNotEmpty() }

        when (role) {
            UserRole.Student -> {
                if (group.isEmpty()) return@combine null
                UserStudent(
                    name = firstName,
                    surname = lastName,
                    middleName = nonEmptyMiddleName,
                    imageUrl = nonEmptyImageUrl,
                    uid = userId,
                    group = group
                )
            }
            UserRole.Teacher -> {
                UserTeacher(
                    name = firstName,
                    surname = lastName,
                    middleName = nonEmptyMiddleName,
                    imageUrl = nonEmptyImageUrl,
                    uid = userId
                )
            }
            UserRole.Undefined -> {
                User(
                    name = firstName,
                    surname = lastName,
                    middleName = nonEmptyMiddleName,
                    imageUrl = nonEmptyImageUrl,
                    uid = userId,
                    role = role
                )
            }
        }
    }
}

suspend fun DataStorePreferences.saveUser(user: User) {
    saveUserId(user.uid ?: return)
    saveUserFirstName(user.name)
    saveUserLastName(user.surname)
    saveUserMiddleName(user.middleName)
    saveUserImageUrl(user.imageUrl)
    saveUserRole(user.role)

    if (user is UserStudent) {
        saveUserGroup(user.group)
    } else {
        saveUserGroup(null)
    }
}

suspend fun DataStorePreferences.clearUserData() {
    remove(USER_ID_KEY)
    remove(USER_EMAIL_KEY)
    remove(USER_PHONE_KEY)
    remove(USER_FIRST_NAME_KEY)
    remove(USER_LAST_NAME_KEY)
    remove(USER_MIDDLE_NAME_KEY)
    remove(USER_ROLE_KEY)
    remove(USER_GROUP_KEY)
    remove(USER_IMAGE_URL_KEY)
    remove(USER_IS_AUTHENTICATED_KEY)
}
