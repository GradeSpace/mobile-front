package org.example.project.features.auth.presentation.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.data.model.user.UserRole
import org.example.project.features.auth.domain.AuthRepository
import org.example.project.features.auth.domain.AuthResult

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Initial)
    val state = _state.asStateFlow()

    private val _navigationEvents = Channel<AuthNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.EmailEntered -> handleEmailEntered(action.email)
            is AuthAction.PasswordEntered -> handlePasswordEntered(action.password)
            is AuthAction.RoleSelected -> handleRoleSelected(action.role)
            is AuthAction.FirstNameEntered -> handleFirstNameEntered(action.firstName)
            is AuthAction.LastNameEntered -> handleLastNameEntered(action.lastName)
            is AuthAction.MiddleNameEntered -> handleMiddleNameEntered(action.middleName)
            is AuthAction.GroupEntered -> handleGroupEntered(action.group)
            is AuthAction.PasswordForRegistrationEntered -> handlePasswordForRegistrationEntered(action.password)
            is AuthAction.ConfirmPasswordEntered -> handleConfirmPasswordEntered(action.password)
            AuthAction.SubmitRegistration -> handleSubmitRegistration()
            AuthAction.BackToEmailInput -> handleBackToEmailInput()
        }
    }

    private fun handleEmailEntered(email: String) {
        if (email.isBlank()) {
            _state.value = AuthState.Initial
            return
        }

        _state.value = AuthState.Loading

        viewModelScope.launch {
            val userExists = repository.checkUserExists(email)

            _state.value = if (userExists) {
                AuthState.PasswordRequired(email)
            } else {
                AuthState.RegistrationRequired(email)
            }
        }
    }

    private fun handlePasswordEntered(password: String) {
        val currentState = _state.value
        if (currentState !is AuthState.PasswordRequired) return

        _state.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.login(currentState.email, password)

            when (result) {
                AuthResult.Success -> {
                    _state.value = AuthState.Authenticated
                }
                AuthResult.InvalidCredentials -> {
                    _state.value = AuthState.PasswordRequired(currentState.email)
                    _navigationEvents.send(AuthNavigationEvent.ShowError("Неверный пароль"))
                }
                AuthResult.UserNotFound -> {
                    _state.value = AuthState.RegistrationRequired(currentState.email)
                    _navigationEvents.send(AuthNavigationEvent.ShowError("Пользователь не найден"))
                }
                is AuthResult.Error -> {
                    _state.value = AuthState.PasswordRequired(currentState.email)
                    _navigationEvents.send(AuthNavigationEvent.ShowError(result.message))
                }
            }
        }
    }

    private fun handleRoleSelected(role: UserRole) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(selectedRole = role)
            } else {
                it
            }
        }
    }

    private fun handleFirstNameEntered(firstName: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(firstName = firstName)
            } else {
                it
            }
        }
    }

    private fun handleLastNameEntered(lastName: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(lastName = lastName)
            } else {
                it
            }
        }
    }

    private fun handleMiddleNameEntered(middleName: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(middleName = middleName)
            } else {
                it
            }
        }
    }

    private fun handleGroupEntered(group: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(group = group)
            } else {
                it
            }
        }
    }

    private fun handlePasswordForRegistrationEntered(password: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(password = password)
            } else {
                it
            }
        }
    }

    private fun handleConfirmPasswordEntered(password: String) {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        _state.update {
            if (it is AuthState.RegistrationRequired) {
                it.copy(confirmPassword = password)
            } else {
                it
            }
        }
    }

    private fun handleSubmitRegistration() {
        val currentState = _state.value
        if (currentState !is AuthState.RegistrationRequired) return

        // Проверяем, что все обязательные поля заполнены
        if (currentState.firstName.isBlank() ||
            currentState.lastName.isBlank() ||
            currentState.password.isBlank() ||
            currentState.confirmPassword.isBlank() ||
            currentState.selectedRole == null) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Заполните все обязательные поля"))
            }
            return
        }

        // Проверяем, что пароли совпадают
        if (currentState.password != currentState.confirmPassword) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Пароли не совпадают"))
            }
            return
        }

        // Проверяем, что для студента указана группа
        if (currentState.selectedRole == UserRole.Student && currentState.group.isBlank()) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Укажите группу"))
            }
            return
        }

        _state.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.register(
                email = currentState.email,
                password = currentState.password,
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                middleName = currentState.middleName.takeIf { it.isNotBlank() },
                role = currentState.selectedRole,
                group = if (currentState.selectedRole == UserRole.Student) currentState.group else null
            )

            when (result) {
                AuthResult.Success -> {
                    _state.value = AuthState.Authenticated
                }
                is AuthResult.Error -> {
                    _state.value = currentState
                    _navigationEvents.send(AuthNavigationEvent.ShowError(result.message))
                }
                else -> {
                    _state.value = currentState
                    _navigationEvents.send(AuthNavigationEvent.ShowError("Произошла ошибка при регистрации"))
                }
            }
        }
    }

    private fun handleBackToEmailInput() {
        _state.value = AuthState.Initial
    }
}
