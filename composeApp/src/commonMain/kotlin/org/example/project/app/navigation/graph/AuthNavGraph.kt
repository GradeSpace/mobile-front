package org.example.project.app.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.example.project.features.auth.navigation.AuthRoute
import org.example.project.features.auth.navigation.authNavGraph

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.AuthScreen,
        modifier = modifier
    ) {
        authNavGraph()
    }
}
