package org.example.project.core.presentation.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.add
import mobile_front.composeapp.generated.resources.add_recipient
import mobile_front.composeapp.generated.resources.all_recipients_selected
import mobile_front.composeapp.generated.resources.available_recipients
import mobile_front.composeapp.generated.resources.close
import mobile_front.composeapp.generated.resources.receivers
import mobile_front.composeapp.generated.resources.remove_recipient
import org.example.project.core.presentation.ui.theme.ChipBlue
import org.example.project.core.presentation.ui.theme.ChipGreen
import org.example.project.core.presentation.ui.theme.ChipPurple
import org.example.project.core.presentation.ui.theme.ChipRed
import org.example.project.core.presentation.ui.theme.ChipYellow
import org.jetbrains.compose.resources.stringResource

private val chipColors = listOf(
    ChipGreen,
    ChipPurple,
    ChipYellow,
    ChipRed,
    ChipBlue
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReceiverSelectorComponent(
    availableReceivers: List<String>,
    selectedReceivers: List<String>,
    maxReceivers: Int = 50,
    onReceiverSelected: (String) -> Unit,
    onReceiverDeselected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onShowReceiversList: ((List<String>, (String) -> Unit) -> Unit)? = null
) {
    var showAvailableReceivers by remember { mutableStateOf(false) }
    val canAddMore = selectedReceivers.size < maxReceivers

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(Res.string.receivers),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "(${selectedReceivers.size}/$maxReceivers)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            selectedReceivers.forEachIndexed { index, receiver ->
                val colorIndex = index % chipColors.size
                val chipColor = chipColors[colorIndex]

                InputChip(
                    selected = true,
                    onClick = { onReceiverDeselected(receiver) },
                    label = { Text(receiver) },
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(Res.string.remove_recipient),
                            modifier = Modifier
                                .size(18.dp)
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        selectedContainerColor = chipColor.copy(alpha = 0.15f),
                        selectedLabelColor = chipColor.copy(alpha = 1f),
                        selectedTrailingIconColor = chipColor.copy(alpha = 1f),
                    )
                )
            }

            if (canAddMore) {
                AssistChip(
                    onClick = {
                        if (onShowReceiversList != null) {
                            val availableToSelect =
                                availableReceivers.filter { it !in selectedReceivers }
                            onShowReceiversList(availableToSelect, onReceiverSelected)
                        } else {
                            showAvailableReceivers = !showAvailableReceivers
                        }
                    },
                    label = { Text(stringResource(Res.string.add)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(Res.string.add_recipient),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

        }

        if (onShowReceiversList == null) {
            AnimatedVisibility(
                visible = showAvailableReceivers && canAddMore,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.available_recipients),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val availableToSelect =
                            availableReceivers.filter { it !in selectedReceivers }

                        if (availableToSelect.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.all_recipients_selected),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                availableToSelect.forEach { receiver ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onReceiverSelected(receiver)
                                            }
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = receiver,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = stringResource(Res.string.add),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            IconButton(
                                onClick = { showAvailableReceivers = false }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.close),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
