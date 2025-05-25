package org.example.project.features.lessons.presentation.lesson_create.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.offline_location
import mobile_front.composeapp.generated.resources.offline_place
import mobile_front.composeapp.generated.resources.online_link
import mobile_front.composeapp.generated.resources.online_location
import org.example.project.core.presentation.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
fun LessonLocationSection(
    isOnlineLocation: Boolean,
    isOfflineLocation: Boolean,
    onlineLink: String,
    offlinePlace: String,
    onOnlineLocationChange: (Boolean) -> Unit,
    onOfflineLocationChange: (Boolean) -> Unit,
    onOnlineLinkChange: (String) -> Unit,
    onOfflinePlaceChange: (String) -> Unit,
    locationError: UiText? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Онлайн локация
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isOnlineLocation,
                onCheckedChange = onOnlineLocationChange
            )

            Text(
                text = stringResource(Res.string.online_location),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    onOnlineLocationChange(!isOnlineLocation)
                }
            )
        }

        AnimatedVisibility(
            visible = isOnlineLocation,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                value = onlineLink,
                onValueChange = onOnlineLinkChange,
                label = { Text(stringResource(Res.string.online_link)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Очная локация
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isOfflineLocation,
                onCheckedChange = onOfflineLocationChange
            )

            Text(
                text = stringResource(Res.string.offline_location),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    onOfflineLocationChange(!isOfflineLocation)
                }
            )
        }

        AnimatedVisibility(
            visible = isOfflineLocation,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                value = offlinePlace,
                onValueChange = onOfflinePlaceChange,
                label = { Text(stringResource(Res.string.offline_place)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
            )
        }

        // Отображение ошибки, если есть
        locationError?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
