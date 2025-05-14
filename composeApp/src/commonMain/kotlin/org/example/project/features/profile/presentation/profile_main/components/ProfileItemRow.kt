package org.example.project.features.profile.presentation.profile_main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.features.profile.domain.ProfileItem
import org.example.project.features.profile.domain.ProfileItemColor
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileItemRow(
    profileItem: ProfileItem,
    modifier: Modifier = Modifier
) {
    val itemColor = when (profileItem.color) {
        ProfileItemColor.DEFAULT -> MaterialTheme.colorScheme.onSurface
        ProfileItemColor.RED -> Color.Red
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = vectorResource(profileItem.iconRes),
            contentDescription = null,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = stringResource(profileItem.title),
                style = MaterialTheme.typography.titleMedium,
                color = itemColor
            )

            profileItem.subtitle?.let { subtitle ->
                Text(
                    text = stringResource(subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = itemColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}