package org.example.project.core.data.model.user

class UserTeacher(
    name: String,
    surname: String,
    middleName: String? = null,
    imageUrl: String? = null,
    uid: String? = null,
) : User(
    name,
    surname,
    middleName,
    imageUrl,
    uid,
    role = UserRole.Teacher
)