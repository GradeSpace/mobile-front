package org.example.project.features.feed.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.features.feed.domain.FeedAction

@Composable
fun FeedActionGroup(
    actions: List<FeedAction>,
    fullWidthSingleButton: Boolean,
    modifier: Modifier = Modifier
) {
    if (actions.isEmpty()) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(top = 8.dp)
    ) {
        if (actions.size == 1 && !fullWidthSingleButton && actions[0] !is FeedAction.PerformedAction) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
        actions.forEach { action ->
            when (action) {
                is FeedAction.ButtonAction -> {
                    FeedActionButton(
                        title = action.actionName,
                        action = action.action,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                is FeedAction.PerformedAction -> {
                    FeedActionPerformedText(
                        title = action.title,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}