package org.example.project.features.feed.presentation.feed_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun FeedListItemActionButton(
    title: String,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = action,
        modifier = modifier
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
