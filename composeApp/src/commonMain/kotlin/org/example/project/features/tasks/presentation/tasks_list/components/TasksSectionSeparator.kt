package org.example.project.features.tasks.presentation.tasks_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun TasksSectionSeparator(
    addTopCorners: Boolean = true,
    addBottomCorners: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.2f))
    ) {
        if (addTopCorners) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        if (addBottomCorners) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(
                        if (addBottomCorners) {
                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        } else {
                            RectangleShape
                        }
                    )
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }
}