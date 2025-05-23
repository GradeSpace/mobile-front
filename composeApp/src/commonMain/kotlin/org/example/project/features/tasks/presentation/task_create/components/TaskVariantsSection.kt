package org.example.project.features.tasks.presentation.task_create.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.add_variant
import mobile_front.composeapp.generated.resources.assign_to_receivers
import mobile_front.composeapp.generated.resources.enable_variants
import mobile_front.composeapp.generated.resources.random_distribution
import mobile_front.composeapp.generated.resources.select_receivers_for_variant
import mobile_front.composeapp.generated.resources.variant
import mobile_front.composeapp.generated.resources.variant_text
import org.example.project.core.presentation.ui.common.ReceiverSelectorComponent
import org.example.project.features.tasks.domain.TaskVariant
import org.example.project.features.tasks.domain.VariantDistributionMode
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskVariantsSection(
    variantDistributionMode: VariantDistributionMode,
    variants: List<TaskVariant>?,
    availableReceivers: List<String>,
    onVariantDistributionModeChange: (VariantDistributionMode) -> Unit,
    onVariantsChange: (List<TaskVariant>?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVariantsEnabled by remember(variants) {
        mutableStateOf(variants != null && variantDistributionMode != VariantDistributionMode.NONE)
    }

    val variantsList = remember(variants) {
        mutableStateListOf<TaskVariant>().apply {
            if (variants != null && variants.isNotEmpty()) {
                addAll(variants)
            } else if (variants == null || variants.isEmpty()) {
                add(TaskVariant(varNum = 1))
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Включение/выключение вариантов
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isVariantsEnabled,
                onCheckedChange = { checked ->
                    isVariantsEnabled = checked
                    if (checked) {
                        if (variantsList.isEmpty()) {
                            variantsList.add(TaskVariant(varNum = 1))
                        }
                        onVariantDistributionModeChange(VariantDistributionMode.RANDOM)
                        onVariantsChange(variantsList.toList())
                    } else {
                        onVariantDistributionModeChange(VariantDistributionMode.NONE)
                        onVariantsChange(null)
                    }
                }
            )

            Text(
                text = stringResource(Res.string.enable_variants),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    isVariantsEnabled = !isVariantsEnabled
                    if (isVariantsEnabled) {
                        if (variantsList.isEmpty()) {
                            variantsList.add(TaskVariant(varNum = 1))
                        }
                        onVariantDistributionModeChange(VariantDistributionMode.RANDOM)
                        onVariantsChange(variantsList.toList())
                    } else {
                        onVariantDistributionModeChange(VariantDistributionMode.NONE)
                    }
                }
            )
        }

        // Секция вариантов
        AnimatedVisibility(
            visible = isVariantsEnabled,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Выбор режима распределения
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    // Случайное распределение
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onVariantDistributionModeChange(VariantDistributionMode.RANDOM)
                                // Обновляем варианты, убирая привязку к получателям
                                val updatedVariants = variantsList.map { it.copy(receivers = null) }
                                variantsList.clear()
                                variantsList.addAll(updatedVariants)
                                onVariantsChange(variantsList.toList())
                            }
                    ) {
                        RadioButton(
                            selected = variantDistributionMode == VariantDistributionMode.RANDOM,
                            onClick = {
                                onVariantDistributionModeChange(VariantDistributionMode.RANDOM)
                                // Обновляем варианты, убирая привязку к получателям
                                val updatedVariants = variantsList.map { it.copy(receivers = null) }
                                variantsList.clear()
                                variantsList.addAll(updatedVariants)
                                onVariantsChange(variantsList.toList())
                            }
                        )

                        Text(
                            text = stringResource(Res.string.random_distribution),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Распределение по получателям
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onVariantDistributionModeChange(VariantDistributionMode.VAR_TO_RECEIVER)
                            }
                    ) {
                        RadioButton(
                            selected = variantDistributionMode == VariantDistributionMode.VAR_TO_RECEIVER,
                            onClick = {
                                onVariantDistributionModeChange(VariantDistributionMode.VAR_TO_RECEIVER)
                            }
                        )

                        Text(
                            text = stringResource(Res.string.assign_to_receivers),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Список вариантов
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    variantsList.forEachIndexed { index, variant ->
                        VariantItem(
                            variant = variant,
                            variantIndex = index,
                            showReceivers = variantDistributionMode == VariantDistributionMode.VAR_TO_RECEIVER,
                            availableReceivers = availableReceivers,
                            onVariantChange = { updatedVariant ->
                                variantsList[index] = updatedVariant
                                onVariantsChange(variantsList.toList())
                            },
                            onDeleteVariant = {
                                if (variantsList.size > 1) {
                                    variantsList.removeAt(index)
                                    for (i in index until variantsList.size) {
                                        variantsList[i] = variantsList[i].copy(varNum = i + 1)
                                    }
                                    onVariantsChange(variantsList.toList())
                                }
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        val newVariantNum = variantsList.size + 1
                        variantsList.add(TaskVariant(varNum = newVariantNum))
                        onVariantsChange(variantsList.toList())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(Res.string.add_variant))
                }
            }
        }
    }
}

@Composable
private fun VariantItem(
    variant: TaskVariant,
    variantIndex: Int,
    showReceivers: Boolean,
    availableReceivers: List<String>,
    onVariantChange: (TaskVariant) -> Unit,
    onDeleteVariant: () -> Unit
) {
    val maxTextLength = 1000

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(Res.string.variant)} ${variant.varNum}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDeleteVariant) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Текст варианта с ограничением и счетчиком
            OutlinedTextField(
                value = variant.text ?: "",
                onValueChange = {
                    if (it.length <= maxTextLength) {
                        onVariantChange(variant.copy(text = it))
                    }
                },
                label = { Text(stringResource(Res.string.variant_text)) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = "${variant.text?.length ?: 0}/$maxTextLength",
                        style = MaterialTheme.typography.bodySmall,
                        color = if ((variant.text?.length ?: 0) >= maxTextLength)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )

            // Выбор получателей для варианта (если включен режим VAR_TO_RECEIVER)
            if (showReceivers) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.select_receivers_for_variant),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ReceiverSelectorComponent(
                    availableReceivers = availableReceivers,
                    selectedReceivers = variant.receivers ?: emptyList(),
                    onReceiverSelected = { receiver ->
                        val currentReceivers = variant.receivers?.toMutableList() ?: mutableListOf()
                        currentReceivers.add(receiver)
                        onVariantChange(variant.copy(receivers = currentReceivers))
                    },
                    onReceiverDeselected = { receiver ->
                        val currentReceivers =
                            variant.receivers?.toMutableList() ?: return@ReceiverSelectorComponent
                        currentReceivers.remove(receiver)
                        onVariantChange(variant.copy(receivers = currentReceivers.ifEmpty { null }))
                    },
                    maxReceivers = 5
                )
            }
        }
    }
}

