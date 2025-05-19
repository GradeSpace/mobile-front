package org.example.project.features.feed.presentation.notification_create.components

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
import org.example.project.core.presentation.ui.theme.ChipBlue
import org.example.project.core.presentation.ui.theme.ChipGreen
import org.example.project.core.presentation.ui.theme.ChipPurple
import org.example.project.core.presentation.ui.theme.ChipRed
import org.example.project.core.presentation.ui.theme.ChipYellow

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
    onReceiverSelected: (String) -> Unit,
    onReceiverDeselected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onShowReceiversList: ((List<String>, (String) -> Unit) -> Unit)? = null
) {
    var showAvailableReceivers by remember { mutableStateOf(false) }
    val maxReceivers = 10
    val canAddMore = selectedReceivers.size < maxReceivers

    Column(modifier = modifier) {
        // Заголовок с информацией о максимальном количестве получателей
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Получатели",
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

        // Выбранные получатели и кнопка добавления
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
                            contentDescription = "Удалить получателя",
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

            // Кнопка добавления нового получателя
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
                    label = { Text("Добавить") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Добавить получателя",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

        }

        // Встроенный список доступных получателей (только если не передан внешний обработчик)
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
                            text = "Доступные получатели",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Если нет доступных получателей или все уже выбраны
                        val availableToSelect =
                            availableReceivers.filter { it !in selectedReceivers }

                        if (availableToSelect.isEmpty()) {
                            Text(
                                text = "Все получатели уже выбраны",
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
                                            contentDescription = "Добавить",
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
                                    contentDescription = "Закрыть",
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
