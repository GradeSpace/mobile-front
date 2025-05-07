package org.example.project.features.tasks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.tasks.presentation.task_screen.TaskRootScreen
import org.example.project.features.tasks.presentation.task_screen.TaskScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.tasksNavGraph(navigationManager: NavigationManager) {
    composable<TasksRoute.TaskScreen> {
        val viewModel = koinViewModel<TaskScreenViewModel>()
        TaskRootScreen(
            viewModel = viewModel,
            navigationManager = navigationManager
        )
    }
}