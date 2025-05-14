package org.example.project.app

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.example.project.app.navigation.graph.RootNavGraph
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.presentation.ui.screen.ScaffoldWithBottomBarWrapper
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val appViewModel = koinViewModel<AppViewModel>()
    val isDarkThemeEnabled = rememberAppTheme(appViewModel)

    GradeSpaceTheme(
        darkTheme = isDarkThemeEnabled
    ) {
        val navController = rememberNavController()
        val navigationManager = NavigationManager(navController)

        ScaffoldWithBottomBarWrapper(
            navigationManager = navigationManager,
            tabs = listOf(
                TabRoute.FeedTab,
                TabRoute.TasksTab,
                TabRoute.CalendarTab,
                TabRoute.ProfileTab
            )
        ) { innerPadding ->
            RootNavGraph(navController, modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()))
        }
    }
}