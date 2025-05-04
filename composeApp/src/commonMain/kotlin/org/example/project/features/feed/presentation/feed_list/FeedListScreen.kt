package org.example.project.features.feed.presentation.feed_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.feed_title
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.presentation.feed_list.components.FeedListBlock
import org.jetbrains.compose.resources.stringResource

@Composable
fun FeedListScreenRoot(
    viewModel: FeedListViewModel,
    navigationManager: NavigationManager
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
    FeedListScreen(state, viewModel::onAction)
}

@Composable
fun FeedListScreen(
    state: FeedListState,
    onAction: (FeedListAction) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Text(
                text = stringResource(Res.string.feed_title),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        items(state.feedBlocks, key = { it.id }) { block ->
            FeedListBlock(
                title = block.title.asString(),
                events = block.events,
                blockType = block.blockType,
                onItemClick = {
                    onAction(FeedListAction.FeedListItemClick(it))
                }
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}