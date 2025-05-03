package org.example.project.core.presentation.ui.screen.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager

@Composable
fun AppBottomBar(
    navigationManager: NavigationManager,
    tabs: List<TabRoute>
) {
    val currentDestination = navigationManager.currentDestination()

    val isBottomBarDestination = tabs.any {
        currentDestination?.hasRoute(it::class) == true
    }

    if (isBottomBarDestination) {
        BottomAppBar(
            modifier = Modifier.height(80.dp)
        ) {
            NavigationBar {
                tabs.map { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navigationManager.navigateTo(item)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}