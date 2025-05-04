package org.example.project.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.example.project.features.feed.data.repository.FeedMockRepository
import org.example.project.features.feed.presentation.feed_list.FeedListScreen
import org.example.project.features.feed.presentation.feed_list.FeedListState

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FeedListScreenPreview() {
    GradeSpaceTheme {
        FeedListScreen(
            state = FeedListState(
                isLoading = false,
                error = null,
                feedBlocks = FeedMockRepository().localEventsBlocks
            ),
            onAction = {}
        )
    }
}