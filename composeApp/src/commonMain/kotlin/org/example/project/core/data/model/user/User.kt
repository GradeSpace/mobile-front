package org.example.project.core.data.model.user

open class User(
    val name: String,
    val surname: String,
    val middleName: String? = null,
    val imageUrl: String? = null,
    val uid: String? = null,
) {
    override fun toString(): String {
        fun makeInitial(text: String?): String {
            return text?.firstOrNull()?.uppercaseChar()?.plus(".") ?: ""
        }

        return buildString {
            append(surname)
            append(" ")
            append(makeInitial(name))
            append(" ")
            append(makeInitial(middleName))
        }
    }
}
