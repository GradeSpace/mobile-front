package org.example.project.features.auth.presentation.auth_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.app_name
import mobile_front.composeapp.generated.resources.back_to_email
import mobile_front.composeapp.generated.resources.confirm_password
import mobile_front.composeapp.generated.resources.continue_button
import mobile_front.composeapp.generated.resources.email
import mobile_front.composeapp.generated.resources.email_format_error
import mobile_front.composeapp.generated.resources.email_hint
import mobile_front.composeapp.generated.resources.enter_email
import mobile_front.composeapp.generated.resources.enter_first_name
import mobile_front.composeapp.generated.resources.enter_group
import mobile_front.composeapp.generated.resources.enter_last_name
import mobile_front.composeapp.generated.resources.enter_middle_name
import mobile_front.composeapp.generated.resources.enter_password
import mobile_front.composeapp.generated.resources.enter_phone
import mobile_front.composeapp.generated.resources.first_name
import mobile_front.composeapp.generated.resources.group
import mobile_front.composeapp.generated.resources.i24_school
import mobile_front.composeapp.generated.resources.last_name
import mobile_front.composeapp.generated.resources.login_title
import mobile_front.composeapp.generated.resources.middle_name
import mobile_front.composeapp.generated.resources.password
import mobile_front.composeapp.generated.resources.password_hint
import mobile_front.composeapp.generated.resources.phone
import mobile_front.composeapp.generated.resources.phone_hint
import mobile_front.composeapp.generated.resources.register
import mobile_front.composeapp.generated.resources.registration_title
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.presentation.UiEventsManager
import org.example.project.core.presentation.UiSnackbar
import org.example.project.core.presentation.UiText
import org.example.project.features.auth.presentation.auth_screen.components.AuthTextField
import org.example.project.features.auth.presentation.auth_screen.components.PasswordTextField
import org.example.project.features.auth.presentation.auth_screen.components.RoleSelectionComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AuthScreenRoot(
    viewModel: AuthViewModel,
    navigationManager: NavigationManager,
    uiEventsManager: UiEventsManager
) {
    val navigationEvents = viewModel.navigationEvents

    LaunchedEffect(Unit) {
        navigationEvents.collect { event ->
            when (event) {
                is AuthNavigationEvent.ShowError -> {
                    uiEventsManager.showSnackBar(
                        UiSnackbar(
                            message = event.message,
                            withDismissAction = true
                        )
                    )
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    AuthScreen(state, viewModel::onAction)
}

enum class AuthMethod { EMAIL, PHONE }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // App logo
                Icon(
                    imageVector = vectorResource(Res.drawable.i24_school),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp)
                )

                // App title
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Auth/registration card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (state) {
                            is AuthState.Initial -> EmailPhoneInputSection(onAction)
                            is AuthState.Loading -> LoadingSection()
                            is AuthState.PasswordRequired -> PasswordInputSection(state, onAction)
                            is AuthState.RegistrationRequired -> RegistrationSection(
                                state,
                                onAction
                            )
                            is AuthState.Authenticated -> {} // Will navigate away
                            is AuthState.Error -> {
                                LaunchedEffect(state) {
                                    snackbarHostState.showSnackbar(
                                        message = state.message,
                                    )
                                }
                                EmailPhoneInputSection(onAction)
                            }
                            is AuthState.EmailEntered -> {}
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                TextButton(onClick = { /* Forgot password action */ }) {
                    Text("Забыли пароль?")
                }
            }
        }
    }
}

// Обновляем только функцию EmailPhoneInputSection
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EmailPhoneInputSection(onAction: (AuthAction) -> Unit) {
    var authMethod by remember { mutableStateOf(AuthMethod.EMAIL) }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showEmailError by remember { mutableStateOf(false) }
    var showPhoneError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Auth method selector
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                selected = authMethod == AuthMethod.EMAIL,
                onClick = {
                    authMethod = AuthMethod.EMAIL
                    // Сбрасываем флаги ошибок при переключении метода
                    showEmailError = false
                    showPhoneError = false
                },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                label = { Text(stringResource(Res.string.email)) }
            )

            SegmentedButton(
                selected = authMethod == AuthMethod.PHONE,
                onClick = {
                    authMethod = AuthMethod.PHONE
                    // Сбрасываем флаги ошибок при переключении метода
                    showEmailError = false
                    showPhoneError = false
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                label = { Text(stringResource(Res.string.phone)) }
            )
        }

        AnimatedVisibility(
            visible = authMethod == AuthMethod.EMAIL,
        ) {
            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    showEmailError = false
                },
                label = stringResource(Res.string.enter_email),
                placeholder = stringResource(Res.string.email_hint),
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        showEmailError = true
                        if (email.contains('@')) {
                            onAction(AuthAction.EmailEntered(email))
                        }
                    }
                ),
                validate = {
                    if (!it.contains('@')) {
                        UiText.StringResourceId(Res.string.email_format_error)
                    } else null
                },
                showErrorImmediately = showEmailError,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(
            visible = authMethod == AuthMethod.PHONE,
        ) {
            AuthTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    showPhoneError = false
                },
                label = stringResource(Res.string.enter_phone),
                placeholder = stringResource(Res.string.phone_hint),
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        showPhoneError = true
                        if (phone.isNotBlank() && phone.length >= 10) {
                            onAction(AuthAction.PhoneEntered(phone))
                        }
                    }
                ),
                validate = {
                    if (it.length < 10) {
                        UiText.DynamicString("Введите корректный номер телефона")
                    } else null
                },
                showErrorImmediately = showPhoneError,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                when (authMethod) {
                    AuthMethod.EMAIL -> {
                        showEmailError = true
                        if (email.contains('@')) {
                            onAction(AuthAction.EmailEntered(email))
                        }
                    }
                    AuthMethod.PHONE -> {
                        showPhoneError = true
                        if (phone.isNotBlank() && phone.length >= 10) {
                            onAction(AuthAction.PhoneEntered(phone))
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                stringResource(Res.string.continue_button),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInputSection(state: AuthState.PasswordRequired, onAction: (AuthAction) -> Unit) {
    var password by remember { mutableStateOf("") }
    var showPasswordError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(AuthAction.BackToEmailInput) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.back_to_email)
                )
            }

            Text(
                text = stringResource(Res.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        // Show user identifier (email or phone)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = if (state.isPhone) Icons.Default.Phone else Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = state.identifier,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        PasswordTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = false
            },
            label = stringResource(Res.string.enter_password),
            placeholder = stringResource(Res.string.password_hint),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    showPasswordError = true
                    if (password.length >= 6) {
                        onAction(AuthAction.PasswordEntered(password))
                    }
                }
            ),
            validate = {
                if (it.length < 6) {
                    UiText.DynamicString("Пароль должен содержать не менее 6 символов")
                } else null
            },
            showErrorImmediately = showPasswordError,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                keyboardController?.hide()
                showPasswordError = true
                if (password.length >= 6) {
                    onAction(AuthAction.PasswordEntered(password))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                stringResource(Res.string.continue_button),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationSection(state: AuthState.RegistrationRequired, onAction: (AuthAction) -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(AuthAction.BackToEmailInput) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.back_to_email)
                )
            }

            Text(
                text = stringResource(Res.string.registration_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        // Show user identifier (email or phone)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = if (state.isPhone) Icons.Default.Phone else Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = state.identifier,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Role selection
        RoleSelectionComponent(
            selectedRole = state.selectedRole,
            onRoleSelected = { onAction(AuthAction.RoleSelected(it)) }
        )

        AnimatedVisibility(
            visible = state.selectedRole != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // First name
                AuthTextField(
                    value = state.firstName,
                    onValueChange = { onAction(AuthAction.FirstNameEntered(it)) },
                    label = stringResource(Res.string.first_name),
                    placeholder = stringResource(Res.string.enter_first_name),
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    validate = {
                        if (it.isBlank()) {
                            UiText.DynamicString("Введите имя")
                        } else null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Last name
                AuthTextField(
                    value = state.lastName,
                    onValueChange = { onAction(AuthAction.LastNameEntered(it)) },
                    label = stringResource(Res.string.last_name),
                    placeholder = stringResource(Res.string.enter_last_name),
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    validate = {
                        if (it.isBlank()) {
                            UiText.DynamicString("Введите фамилию")
                        } else null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Middle name (optional)
                AuthTextField(
                    value = state.middleName,
                    onValueChange = { onAction(AuthAction.MiddleNameEntered(it)) },
                    label = stringResource(Res.string.middle_name),
                    placeholder = stringResource(Res.string.enter_middle_name),
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Group (only for students)
                AnimatedVisibility(visible = state.selectedRole == UserRole.Student) {
                    AuthTextField(
                        value = state.group,
                        onValueChange = { onAction(AuthAction.GroupEntered(it)) },
                        label = stringResource(Res.string.group),
                        placeholder = stringResource(Res.string.enter_group),
                        leadingIcon = vectorResource(Res.drawable.i24_school),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        validate = {
                            if (it.isBlank() && state.selectedRole == UserRole.Student) {
                                UiText.DynamicString("Введите группу")
                            } else null
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Password
                PasswordTextField(
                    value = state.password,
                    onValueChange = { onAction(AuthAction.PasswordForRegistrationEntered(it)) },
                    label = stringResource(Res.string.password),
                    placeholder = stringResource(Res.string.password_hint),
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    validate = {
                        when {
                            it.length < 9 -> UiText.DynamicString("Минимум 9 символов")
                            !it.any { c -> c.isLetter() } -> UiText.DynamicString("Добавьте хотя бы одну букву")
                            !it.any { c -> c.isDigit() } -> UiText.DynamicString("Добавьте хотя бы одну цифру")
                            else -> null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Confirm password
                PasswordTextField(
                    value = state.confirmPassword,
                    onValueChange = { onAction(AuthAction.ConfirmPasswordEntered(it)) },
                    label = stringResource(Res.string.confirm_password),
                    placeholder = "",
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (isRegistrationFormValid(state)) {
                                onAction(AuthAction.SubmitRegistration)
                            }
                        }
                    ),
                    validate = {
                        if (it != state.password) {
                            UiText.DynamicString("Пароли не совпадают")
                        } else null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (isRegistrationFormValid(state)) {
                            onAction(AuthAction.SubmitRegistration)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        stringResource(Res.string.register),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

fun isRegistrationFormValid(state: AuthState.RegistrationRequired): Boolean {
    // Check required fields
    if (state.firstName.isBlank() || state.lastName.isBlank() ||
        state.password.isBlank() || state.confirmPassword.isBlank() ||
        state.selectedRole == null) {
        return false
    }

    // Check group for students
    if (state.selectedRole == UserRole.Student && state.group.isBlank()) {
        return false
    }

    // Check password match
    if (state.password != state.confirmPassword) {
        return false
    }

    // Check password complexity
    if (state.password.length < 9 ||
        !state.password.any { it.isLetter() } ||
        !state.password.any { it.isDigit() }) {
        return false
    }

    return true
}

@Composable
fun LoadingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}
