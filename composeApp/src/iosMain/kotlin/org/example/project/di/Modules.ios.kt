package org.example.project.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.example.project.app.domain.Localization
import org.example.project.core.data.database.DatabaseFactory
import org.example.project.core.data.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> {
            Darwin.create()
        }
        single { DatabaseFactory() }
        single { createDataStore() }
        single { Localization() }
    }