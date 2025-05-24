package org.example.project.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first
import org.example.project.app.navigation.graph.AuthNavGraph
import org.example.project.app.navigation.graph.RootNavGraph
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.domain.repository.UserRepository
import org.example.project.core.presentation.UiEventsManager
import org.example.project.core.presentation.ui.screen.ScaffoldWithBottomBarWrapper
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val appViewModel = koinViewModel<AppViewModel>()
    val userRepository = koinInject<UserRepository>()

    // Состояние для отслеживания инициализации приложения
    var isAppInitialized by remember { mutableStateOf(false) }

    // Получаем статус аутентификации как поток
    val authState by userRepository.isAuthenticatedAsFlow().collectAsStateWithLifecycle(null)

    // Инициализируем приложение при запуске
    LaunchedEffect(Unit) {
        // Проверяем статус аутентификации и инициализируем его, если нужно
        val currentAuthStatus = userRepository.isAuthenticatedAsFlow().first()
        if (currentAuthStatus == null) {
            userRepository.saveUserAuthStatus(false)
        }

        // Инициализируем тему и язык, если нужно
        // Эти вызовы гарантируют, что данные будут загружены
        appViewModel.ensureThemeInitialized()
        appViewModel.ensureLanguageInitialized()

        // Отмечаем, что инициализация завершена
        isAppInitialized = true
    }

    // Показываем SplashScreen, пока не завершена инициализация или не получен статус аутентификации
    if (!isAppInitialized || authState == null) {
        SplashScreen()
        return
    }

    // Получаем тему после инициализации
    val isDarkThemeEnabled = rememberAppTheme(appViewModel)

    GradeSpaceTheme(
        darkTheme = isDarkThemeEnabled
    ) {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        val navigationManager = remember {
            NavigationManager(navController)
        }

        val uiEventsManager = remember {
            UiEventsManager(snackbarHostState)
        }

        CompositionLocalProvider(
            LocalNavigationManager provides navigationManager,
            LocalUiEventsManager provides uiEventsManager,
        ) {
            ScaffoldWithBottomBarWrapper(
                navigationManager = navigationManager,
                snackbarHostState = snackbarHostState,
                tabs = listOf(
                    TabRoute.FeedTab,
                    TabRoute.TasksTab,
                    TabRoute.CalendarTab,
                    TabRoute.ProfileTab
                ),
                withBottomBar = authState == true
            ) { innerPadding ->
                if (authState == true) {
                    RootNavGraph(
                        navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                } else {
                    AuthNavGraph(
                        navController
                    )
                }
            }
        }
    }
}
