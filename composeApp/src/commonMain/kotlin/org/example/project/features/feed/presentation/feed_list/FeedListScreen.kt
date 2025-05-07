package org.example.project.features.feed.presentation.feed_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.feed_title
import org.example.project.Platform
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.domain.EventItemTimeFormat
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.EventListItem
import org.example.project.core.presentation.ui.common.TitleChip
import org.example.project.features.feed.domain.FeedEventsBlock.BlockType
import org.example.project.features.feed.presentation.components.FeedActionGroup
import org.example.project.getPlatform
import org.jetbrains.compose.resources.stringResource

@Composable
fun FeedListScreenRoot(
    viewModel: FeedListViewModel,
    navigationManager: NavigationManager,
    modifier: Modifier
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is FeedListNavigationEvent.NavigateTo ->
                    navigationManager.navigateTo(navEvent.route)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    FeedListScreen(
        state,
        viewModel::onAction,
        modifier
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedListScreen(
    state: FeedListState,
    onAction: (FeedListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (getPlatform() == Platform.Desktop) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.feed_title),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        )
                    },
                    actions = {
                        DesktopRefreshButton {
                            onAction(FeedListAction.OnPullToRefresh)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(FeedListAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(
                    top = if (getPlatform() == Platform.Desktop) {
                        innerPadding.calculateTopPadding()
                    } else 0.dp
                )
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)

            ) {
                val modifier = Modifier.widthIn(max = 600.dp)
                if (getPlatform() != Platform.Desktop) {
                    item {
                        Spacer(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                    item {
                        Text(
                            text = stringResource(Res.string.feed_title),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = modifier
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
                state.feedBlocks.forEach { block ->
                    stickyHeader(key = "feed_sticky_header_${block.id}") {
                        TitleChip(
                            text = block.title.map { it.asString() }.joinToString(" "),
                            modifier = modifier
                                .padding(top = 4.dp)
                                .padding(bottom = 2.dp)
                                .padding(vertical = 4.dp)
                        )
                    }
                    itemsIndexed(
                        items = block.events,
                        key = { _, event ->
                            event.id
                        }
                    ) { i, event ->
                        EventListItem(
                            eventItem = event,
                            timeFormat = if (block.blockType == BlockType.OLD) {
                                EventItemTimeFormat.FULL
                            } else EventItemTimeFormat.COMPACT,
                            onClick = {
                                onAction(FeedListAction.FeedListItemClick(event))
                            },
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            if (block.blockType != BlockType.OLD && event.actions.isNotEmpty()) {
                                FeedActionGroup(event.actions, fullWidthSingleButton = false)
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(12.dp)
                                )
                            }
                        }
                        if (i == block.events.lastIndex) {
                            Spacer(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            )
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
                onClick = { onAction(FeedListAction.CreateNewNotification) },
            ) { Icon(Icons.Default.Add, null) }
        }
    }
}