package org.example.project.core.data.model.user

open class User(
    val name: String,
    val surname: String,
    val middleName: String? = null,
    val imageUrl: String? = null,
    val uid: String? = null,
    val role: UserRole = UserRole.Undefined
) {
    fun toShortName(): String {
        fun makeInitial(text: String?): String {
            return text?.firstOrNull()?.uppercaseChar()?.plus(".") ?: ""
        }

        return buildString {
            append(surname)
            append(" ")
            append(makeInitial(name))
            middleName?.let {
                append(" ")
                append(makeInitial(middleName))
            }
        }
    }

    fun toFullName(): String {
        return buildString {
            append(surname)
            append(" ")
            append(name)
            middleName?.let {
                append(" ")
                append(middleName)
            }
        }
    }

}
