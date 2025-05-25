package org.example.project.features.lessons.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.LocalNavigationManager
import org.example.project.app.LocalUiEventsManager
import org.example.project.features.lessons.presentation.lesson.LessonScreenRoot
import org.example.project.features.lessons.presentation.lesson.LessonScreenViewModel
import org.example.project.features.lessons.presentation.lesson_create.LessonCreateRootScreen
import org.example.project.features.lessons.presentation.lesson_create.LessonCreateViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.lessonsNavGraph() {
    composable<LessonRoutes.Lesson> {
        val navigationManager = LocalNavigationManager.current
        val viewModel = koinViewModel<LessonScreenViewModel>()
        LessonScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager
        )
    }

    composable<LessonRoutes.LessonCreate> {
        val navigationManager = LocalNavigationManager.current
        val uiEventManager = LocalUiEventsManager.current
        val viewModel = koinViewModel<LessonCreateViewModel>()
        LessonCreateRootScreen(
            viewModel,
            navigationManager,
            uiEventManager
        )
    }
}