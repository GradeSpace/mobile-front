package org.example.project.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.example.project.app.domain.Localization
import org.example.project.core.data.database.DatabaseFactory
import org.example.project.core.data.datastore.createDatastore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
        singleOf(::createDatastore)
        singleOf(::Localization)
    }