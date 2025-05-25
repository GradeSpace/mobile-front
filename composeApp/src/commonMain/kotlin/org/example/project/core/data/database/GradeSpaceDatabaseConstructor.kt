package org.example.project.core.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object GradeSpaceDatabaseConstructor: RoomDatabaseConstructor<GradeSpaceDatabase> {
    override fun initialize(): GradeSpaceDatabase
}