package org.example.project.features.auth.presentation.auth_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.app_name
import mobile_front.composeapp.generated.resources.back_to_email
import mobile_front.composeapp.generated.resources.confirm_password
import mobile_front.composeapp.generated.resources.continue_button
import mobile_front.composeapp.generated.resources.email_hint
import mobile_front.composeapp.generated.resources.enter_email
import mobile_front.composeapp.generated.resources.enter_first_name
import mobile_front.composeapp.generated.resources.enter_group
import mobile_front.composeapp.generated.resources.enter_last_name
import mobile_front.composeapp.generated.resources.enter_middle_name
import mobile_front.composeapp.generated.resources.enter_password
import mobile_front.composeapp.generated.resources.first_name
import mobile_front.composeapp.generated.resources.group
import mobile_front.composeapp.generated.resources.i24_school
import mobile_front.composeapp.generated.resources.last_name
import mobile_front.composeapp.generated.resources.login_title
import mobile_front.composeapp.generated.resources.middle_name
import mobile_front.composeapp.generated.resources.password
import mobile_front.composeapp.generated.resources.password_hint
import mobile_front.composeapp.generated.resources.register
import mobile_front.composeapp.generated.resources.registration_title
import mobile_front.composeapp.generated.resources.select_role
import mobile_front.composeapp.generated.resources.student
import mobile_front.composeapp.generated.resources.teacher
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.presentation.UiEventsManager
import org.example.project.core.presentation.UiSnackbar
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
                            duration = SnackbarDuration.Short,
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

@Composable
fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

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
                // Заголовок приложения
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Карточка с формой авторизации/регистрации
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (state) {
                            is AuthState.Initial -> EmailInputSection(onAction)
                            is AuthState.Loading -> LoadingSection()
                            is AuthState.PasswordRequired -> PasswordInputSection(state, onAction)
                            is AuthState.RegistrationRequired -> RegistrationSection(
                                state,
                                onAction
                            )

                            is AuthState.Authenticated -> {} // Ничего не показываем, будет навигация
                            is AuthState.Error -> {
                                LaunchedEffect(state) {
                                    snackbarHostState.showSnackbar(
                                        message = state.message,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                EmailInputSection(onAction)
                            }

                            is AuthState.EmailEntered -> {
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmailInputSection(onAction: (AuthAction) -> Unit) {
    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(Res.string.enter_email)) },
            placeholder = { Text(stringResource(Res.string.email_hint)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onAction(AuthAction.EmailEntered(email))
                }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(AuthAction.EmailEntered(email)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.continue_button))
        }
    }
}

@Composable
fun PasswordInputSection(state: AuthState.PasswordRequired, onAction: (AuthAction) -> Unit) {
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.email,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(Res.string.enter_password)) },
            placeholder = { Text(stringResource(Res.string.password_hint)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onAction(AuthAction.PasswordEntered(password))
                }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(AuthAction.PasswordEntered(password)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.continue_button))
        }
    }
}

@Composable
fun RegistrationSection(state: AuthState.RegistrationRequired, onAction: (AuthAction) -> Unit) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.email,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор роли
        Text(
            text = stringResource(Res.string.select_role),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = state.selectedRole == UserRole.Student,
                onClick = { onAction(AuthAction.RoleSelected(UserRole.Student)) }
            )
            Text(
                text = stringResource(Res.string.student),
                modifier = Modifier.clickable { onAction(AuthAction.RoleSelected(UserRole.Student)) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = state.selectedRole == UserRole.Teacher,
                onClick = { onAction(AuthAction.RoleSelected(UserRole.Teacher)) }
            )
            Text(
                text = stringResource(Res.string.teacher),
                modifier = Modifier.clickable { onAction(AuthAction.RoleSelected(UserRole.Teacher)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поля для ввода данных пользователя
        AnimatedVisibility(
            visible = state.selectedRole != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Имя
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { onAction(AuthAction.FirstNameEntered(it)) },
                    label = { Text(stringResource(Res.string.first_name)) },
                    placeholder = { Text(stringResource(Res.string.enter_first_name)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Фамилия
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { onAction(AuthAction.LastNameEntered(it)) },
                    label = { Text(stringResource(Res.string.last_name)) },
                    placeholder = { Text(stringResource(Res.string.enter_last_name)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Отчество (необязательно)
                OutlinedTextField(
                    value = state.middleName,
                    onValueChange = { onAction(AuthAction.MiddleNameEntered(it)) },
                    label = { Text(stringResource(Res.string.middle_name)) },
                    placeholder = { Text(stringResource(Res.string.enter_middle_name)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )

                // Группа (только для студентов)
                AnimatedVisibility(visible = state.selectedRole == UserRole.Student) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = state.group,
                            onValueChange = { onAction(AuthAction.GroupEntered(it)) },
                            label = { Text(stringResource(Res.string.group)) },
                            placeholder = { Text(stringResource(Res.string.enter_group)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.i24_school),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Пароль
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onAction(AuthAction.PasswordForRegistrationEntered(it)) },
                    label = { Text(stringResource(Res.string.password)) },
                    placeholder = { Text(stringResource(Res.string.password_hint)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Подтверждение пароля
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { onAction(AuthAction.ConfirmPasswordEntered(it)) },
                    label = { Text(stringResource(Res.string.confirm_password)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onAction(AuthAction.SubmitRegistration)
                        }
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onAction(AuthAction.SubmitRegistration) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.register))
                }
            }
        }
    }
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
