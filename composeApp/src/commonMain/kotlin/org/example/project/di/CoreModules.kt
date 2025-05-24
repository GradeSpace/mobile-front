package org.example.project.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.project.app.AppViewModel
import org.example.project.core.data.database.DatabaseFactory
import org.example.project.core.data.database.GradeSpaceDatabase
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.datastore.DataStorePreferencesImpl
import org.example.project.core.data.network.HttpClientFactory
import org.example.project.core.data.repository.UserRepositoryImpl
import org.example.project.core.domain.repository.UserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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
    single { DataStorePreferencesImpl(get()) }.bind<DataStorePreferences>()

    single { UserRepositoryImpl(get()) }.bind<UserRepository>()
    viewModelOf(::AppViewModel)
}