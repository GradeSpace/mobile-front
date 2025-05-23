package org.example.project.features.tasks.presentation.task_create.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.enable_grading
import mobile_front.composeapp.generated.resources.max_grade
import mobile_front.composeapp.generated.resources.min_grade
import org.example.project.core.data.model.note.Grade
import org.example.project.core.data.model.note.GradeRange
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskGradeSection(
    gradeRange: GradeRange?,
    onGradeRangeChange: (GradeRange?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isGradingEnabled by remember(gradeRange) {
        mutableStateOf(gradeRange != null)
    }

    // Сохраняем значения полей даже если секция отключена
    var minGradeText by remember(gradeRange?.minGrade) {
        mutableStateOf(gradeRange?.minGrade?.value?.toString() ?: "")
    }

    var maxGradeText by remember(gradeRange?.maxGrade) {
        mutableStateOf(gradeRange?.maxGrade?.value?.toString() ?: "")
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isGradingEnabled,
                onCheckedChange = { checked ->
                    isGradingEnabled = checked
                    if (!checked) {
                        // Не удаляем данные, просто отключаем секцию
                        onGradeRangeChange(null)
                    } else if (maxGradeText.isNotEmpty()) {
                        // Восстанавливаем предыдущие значения
                        val maxGrade = maxGradeText.toDoubleOrNull()?.let { Grade(it) }
                        val minGrade = minGradeText.toDoubleOrNull()?.let { Grade(it) }
                        onGradeRangeChange(GradeRange(minGrade = minGrade, maxGrade = maxGrade))
                    }
                }
            )

            Text(
                text = stringResource(Res.string.enable_grading),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    isGradingEnabled = !isGradingEnabled
                    if (!isGradingEnabled) {
                        onGradeRangeChange(null)
                    } else if (maxGradeText.isNotEmpty()) {
                        val maxGrade = maxGradeText.toDoubleOrNull()?.let { Grade(it) }
                        val minGrade = minGradeText.toDoubleOrNull()?.let { Grade(it) }
                        onGradeRangeChange(GradeRange(minGrade = minGrade, maxGrade = maxGrade))
                    }
                }
            )
        }

        if (isGradingEnabled) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Минимальная оценка (опциональная)
                OutlinedTextField(
                    value = minGradeText,
                    onValueChange = { value ->
                        val filteredValue = value.filter { it.isDigit() || it == '.' }
                        if (filteredValue.count { it == '.' } <= 1) {
                            val parts = filteredValue.split('.')
                            if (parts.size == 1 || parts[1].length <= 2) {
                                minGradeText = filteredValue
                                val minGrade = filteredValue.toDoubleOrNull()?.let { Grade(it) }
                                val maxGrade = maxGradeText.toDoubleOrNull()?.let { Grade(it) }
                                if (maxGrade != null) {
                                    onGradeRangeChange(GradeRange(minGrade = minGrade, maxGrade = maxGrade))
                                }
                            }
                        }
                    },
                    label = { Text(stringResource(Res.string.min_grade)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Максимальная оценка (обязательная)
                OutlinedTextField(
                    value = maxGradeText,
                    onValueChange = { value ->
                        // Фильтруем ввод, разрешая только числа с плавающей точкой
                        val filteredValue = value.filter { it.isDigit() || it == '.' }
                        // Проверяем, что точка только одна и не более двух знаков после запятой
                        if (filteredValue.count { it == '.' } <= 1) {
                            val parts = filteredValue.split('.')
                            if (parts.size == 1 || parts[1].length <= 2) {
                                maxGradeText = filteredValue
                                val maxGrade = filteredValue.toDoubleOrNull()?.let { Grade(it) }
                                val minGrade = minGradeText.toDoubleOrNull()?.let { Grade(it) }
                                if (maxGrade != null) {
                                    onGradeRangeChange(GradeRange(minGrade = minGrade, maxGrade = maxGrade))
                                } else {
                                    // Если максимальная оценка пуста, не сбрасываем GradeRange полностью
                                    onGradeRangeChange(GradeRange(minGrade = minGrade, maxGrade = null))
                                }
                            }
                        }
                    },
                    label = { Text(stringResource(Res.string.max_grade)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
