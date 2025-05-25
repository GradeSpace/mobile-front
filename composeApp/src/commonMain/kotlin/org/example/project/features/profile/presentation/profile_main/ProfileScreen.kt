package org.example.project.features.profile.presentation.profile_main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.profile.presentation.components.ProfileBottomSheet
import org.example.project.features.profile.presentation.profile_main.components.ProfileItemRow
import org.example.project.features.profile.presentation.profile_main.components.UserInfoCard

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                else -> {}
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileScreen(
        state,
        viewModel::onAction,
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileBottomSheet(
        isBottomSheetVisible = state.isBottomSheetVisible,
        bottomSheetContent = state.bottomSheetContent,
        onDismissRequest = { onAction(ProfileAction.HideBottomSheet) }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(ProfileAction.OnPullToRefresh) },
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Column {
                UserInfoCard(
                    user = state.userInfo
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 16.dp)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = 16.dp)
                ) {
                    itemsIndexed(
                        state.profileItems,
                        key = { _, item -> item.item.name }
                    ) { index, item ->
                        ProfileItemRow(
                            profileItem = item.item,
                            modifier = Modifier
                                .clickable { item.action() }
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }

        }
    }
}