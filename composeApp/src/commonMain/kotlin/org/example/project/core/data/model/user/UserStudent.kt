package org.example.project.core.data.model.user

class UserStudent(
    name: String,
    surname: String,
    middleName: String? = null,
    imageUrl: String? = null,
    uid: String? = null,
    val group: String
) : User(
    name,
    surname,
    middleName,
    imageUrl,
    uid,
    role = UserRole.Student
)