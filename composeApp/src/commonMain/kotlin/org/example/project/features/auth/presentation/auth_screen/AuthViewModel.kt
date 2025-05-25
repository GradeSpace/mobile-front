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
            is AuthAction.PhoneEntered -> handlePhoneEntered(action.phone)
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
        if (email.isBlank() || !email.contains('@')) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Введите корректный email"))
            }
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

    private fun handlePhoneEntered(phone: String) {
        if (phone.isBlank() || phone.length < 10) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Введите корректный номер телефона"))
            }
            return
        }

        _state.value = AuthState.Loading

        viewModelScope.launch {
            // Здесь должна быть проверка существования пользователя по телефону
            // Для примера используем ту же логику, что и для email
            val userExists = repository.checkUserExists(phone)

            _state.value = if (userExists) {
                AuthState.PasswordRequired(phone, isPhone = true)
            } else {
                AuthState.RegistrationRequired(phone, isPhone = true)
            }
        }
    }

    private fun handlePasswordEntered(password: String) {
        val currentState = _state.value
        if (currentState !is AuthState.PasswordRequired) return

        if (password.length < 6) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Пароль должен содержать не менее 6 символов"))
            }
            return
        }

        _state.value = AuthState.Loading

        viewModelScope.launch {
            // Используем email или телефон в зависимости от того, что было введено
            val identifier = currentState.identifier
            val result = repository.login(identifier, password)

            when (result) {
                AuthResult.Success -> {
                    _state.value = AuthState.Authenticated
                }
                AuthResult.InvalidCredentials -> {
                    _state.value = currentState
                    _navigationEvents.send(AuthNavigationEvent.ShowError("Неверный пароль"))
                }
                AuthResult.UserNotFound -> {
                    _state.value = AuthState.RegistrationRequired(
                        identifier = currentState.identifier,
                        isPhone = currentState.isPhone
                    )
                    _navigationEvents.send(AuthNavigationEvent.ShowError("Пользователь не найден"))
                }
                is AuthResult.Error -> {
                    _state.value = currentState
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

        // Проверяем сложность пароля
        if (currentState.password.length < 9 ||
            !currentState.password.any { it.isLetter() } ||
            !currentState.password.any { it.isDigit() }) {
            viewModelScope.launch {
                _navigationEvents.send(AuthNavigationEvent.ShowError("Пароль должен содержать не менее 9 символов, включая хотя бы одну букву и одну цифру"))
            }
            return
        }

        _state.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.register(
                email = if (!currentState.isPhone) currentState.identifier else "",
                phone = if (currentState.isPhone) currentState.identifier else null,
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
