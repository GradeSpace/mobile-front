package org.example.project.core.presentation.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import org.example.project.Platform
import org.example.project.getPlatform

@Composable
fun DesktopRefreshButton(
    onClick: () -> Unit
) {
    if (getPlatform() == Platform.Desktop) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
    }
}