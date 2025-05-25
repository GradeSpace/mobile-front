package org.example.project.di

import org.example.project.features.auth.di.authModule
import org.example.project.features.feed.di.feedModule
import org.example.project.features.lessons.di.lessonsModule
import org.example.project.features.profile.di.profileModule
import org.example.project.features.tasks.di.tasksModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            // Основные модули
            coreModule,
            platformModule,

            // Фича модули
            feedModule,
            tasksModule,
            lessonsModule,
            profileModule,
            authModule
        )
    }
}