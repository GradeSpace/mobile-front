package org.example.project.app.navigation.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.compose_multiplatform
import org.example.project.Greeting
import org.example.project.app.navigation.route.FeedRoutes
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.jetbrains.compose.resources.painterResource

fun NavGraphBuilder.tabsNavGraph(navigationManager: NavigationManager) {
    composable<TabRoute.FeedTab> {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
                navigationManager.navigateToFeedScreen(FeedRoutes.FeedMain)
            }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
    composable<TabRoute.ProfileTab> {

    }
    composable<TabRoute.TasksTab> {

    }
    composable<TabRoute.CalendarTab> {

    }
}