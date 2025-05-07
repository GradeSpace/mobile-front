package org.example.project.features.tasks.di

import org.example.project.features.tasks.data.repository.MockTasksRepository
import org.example.project.features.tasks.domain.TasksRepository
import org.example.project.features.tasks.presentation.task_screen.TaskScreenViewModel
import org.example.project.features.tasks.presentation.tasks_list.TasksListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tasksModule = module {
    singleOf(::MockTasksRepository).bind<TasksRepository>()
    viewModelOf(::TasksListViewModel)
    viewModelOf(::TaskScreenViewModel)
}