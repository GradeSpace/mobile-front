package org.example.project.features.feed.presentation.feed_notification.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.features.feed.domain.FeedAction
import org.example.project.features.feed.presentation.components.FeedActionGroup

@Composable
fun NotificationBottomBar(
    actions: List<FeedAction>,
) {
    if (actions.isEmpty()) return
    BottomAppBar(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        FeedActionGroup(
            actions,
            fullWidthSingleButton = true,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}
