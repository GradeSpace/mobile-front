package org.example.project.features.auth.presentation.auth_screen.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.i24_visibility
import mobile_front.composeapp.generated.resources.i24_visibility_off
import org.example.project.core.presentation.UiText
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Done,
    errorText: UiText? = null,
    validate: ((String) -> UiText?)? = null,
    showErrorImmediately: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = Icons.Default.Lock,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible)
                        vectorResource(Res.drawable.i24_visibility)
                    else
                        vectorResource(Res.drawable.i24_visibility_off),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        errorText = errorText,
        validate = validate,
        showErrorImmediately = showErrorImmediately,
        modifier = modifier
    )
}
