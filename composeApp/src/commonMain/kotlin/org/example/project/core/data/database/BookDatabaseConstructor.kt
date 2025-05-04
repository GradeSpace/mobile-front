package org.example.project.core.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object BookDatabaseConstructor: RoomDatabaseConstructor<GradeSpaceDatabase> {
    override fun initialize(): GradeSpaceDatabase
}