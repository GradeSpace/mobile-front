package org.example.project.features.auth.presentation.auth_screen.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import org.example.project.core.presentation.UiText

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorText: UiText? = null,
    validate: ((String) -> UiText?)? = null,
    showErrorImmediately: Boolean = false
) {
    var showError by remember { mutableStateOf(showErrorImmediately) }
    var currentError by remember { mutableStateOf<UiText?>(null) }
    var isFocused by remember { mutableStateOf(false) }
    var wasSubmitted by remember { mutableStateOf(false) }

    // Сбрасываем флаг отображения ошибки при изменении значения
    LaunchedEffect(value) {
        if (showError && !wasSubmitted) {
            showError = false
        }

        // Если поле в фокусе и есть валидатор, обновляем текущую ошибку
        if (validate != null) {
            currentError = validate(value)
        }
    }

    // Определяем, нужно ли показывать ошибку
    val displayError = if (showError) {
        errorText ?: currentError
    } else null

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            // При изменении значения скрываем ошибку, если не было отправки формы
            if (!wasSubmitted) {
                showError = false
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = trailingIcon,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused

            // Показываем ошибку при потере фокуса, если поле было заполнено
            if (!focusState.isFocused && value.isNotEmpty() && validate != null) {
                currentError = validate(value)
                showError = true
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                wasSubmitted = true
                showError = true
                keyboardActions.onDone?.invoke(this)
            },
            onGo = {
                wasSubmitted = true
                showError = true
                keyboardActions.onGo?.invoke(this)
            },
            onNext = {
                wasSubmitted = true
                showError = true
                keyboardActions.onNext?.invoke(this)
            },
            onPrevious = {
                keyboardActions.onPrevious?.invoke(this)
            },
            onSearch = {
                wasSubmitted = true
                showError = true
                keyboardActions.onSearch?.invoke(this)
            },
            onSend = {
                wasSubmitted = true
                showError = true
                keyboardActions.onSend?.invoke(this)
            }
        ),
        singleLine = singleLine,
        isError = isError || displayError != null,
        supportingText = {
            displayError?.let {
                Text(
                    text = it.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
