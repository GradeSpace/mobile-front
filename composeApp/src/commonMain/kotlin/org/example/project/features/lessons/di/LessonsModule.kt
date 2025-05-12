package org.example.project.features.lessons.di

import org.example.project.features.lessons.data.repository.LessonMockRepository
import org.example.project.features.lessons.domain.LessonRepository
import org.example.project.features.lessons.presentation.calendar.CalendarViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val lessonsModule = module {
    singleOf(::LessonMockRepository).bind<LessonRepository>()
    viewModelOf(::CalendarViewModel)
}