package org.example.project.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.project.core.data.database.DatabaseFactory
import org.example.project.core.data.database.GradeSpaceDatabase
import org.example.project.core.data.network.HttpClientFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val coreModule = module {
    single { HttpClientFactory.create(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<GradeSpaceDatabase>().feedDao }
    single { get<GradeSpaceDatabase>().tasksDao }
    single { get<GradeSpaceDatabase>().calendarDao }
    single { get<GradeSpaceDatabase>().profileDao }
}