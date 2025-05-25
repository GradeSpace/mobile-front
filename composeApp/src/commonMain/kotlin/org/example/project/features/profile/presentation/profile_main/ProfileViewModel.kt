package org.example.project.features.profile.presentation.profile_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.language_change
import mobile_front.composeapp.generated.resources.theme_change
import org.example.project.core.data.model.Action
import org.example.project.core.data.model.Language
import org.example.project.core.data.model.Theme
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.UiText.StringResourceId
import org.example.project.core.presentation.toUiText
import org.example.project.features.profile.domain.ProfileItem
import org.example.project.features.profile.domain.ProfileRepository
import org.example.project.features.profile.presentation.components.BottomSheetContent
import org.example.project.features.profile.presentation.components.BottomSheetItem

class ProfileViewModel(
    val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeProfileItemsJob: Job? = null
    private var observeUserInfoJob: Job? = null
    private var observeThemesJob: Job? = null
    private var observeLanguagesJob: Job? = null

    init {
        observeProfileItems()
        observeUserInfo()
        actualizeProfileItems()
    }

    private fun actualizeProfileItems() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(1000L)
            repository.actualize()
                .onSuccess {
                    observeUserInfo()
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            error = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun observeProfileItems() {
        observeProfileItemsJob?.cancel()
        observeProfileItemsJob = repository
            .getProfileItems()
            .distinctUntilChanged()
            .onEach { profileItems ->
                _state.update {
                    it.copy(
                        profileItems = profileItems.map { it.toUiProfileItem() },
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUserInfo() {
        observeUserInfoJob?.cancel()
        observeUserInfoJob = repository
            .getUserInfo()
            .distinctUntilChanged()
            .onEach { user ->
                _state.update {
                    it.copy(
                        userInfo = user,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun ProfileItem.toUiProfileItem(): UiProfileItem {
        val action: Action = when (this) {
            ProfileItem.PERSONAL_DATA -> {
                {

                }
            }

            ProfileItem.RUSSIAN_LANGUAGE, ProfileItem.ENGLISH_LANGUAGE -> {
                {
                    onAction(ProfileAction.ShowLanguageBottomSheet)
                }
            }

            ProfileItem.DARK_THEME, ProfileItem.LIGHT_THEME, ProfileItem.SYSTEM_THEME -> {
                {
                    onAction(ProfileAction.ShowThemeBottomSheet)
                }
            }

            ProfileItem.EXIT -> {
                {
                    onAction(ProfileAction.ExitApp)
                }
            }
        }
        return UiProfileItem(
            item = this,
            action = action
        )
    }

    fun onAction(action: ProfileAction) = viewModelScope.launch {
        when (action) {
            is ProfileAction.ChangeTheme -> repository.changeTheme(action.theme)
            is ProfileAction.ChangeLanguage -> repository.changeLanguage(action.language)
            ProfileAction.OnPullToRefresh -> actualizeProfileItems()
            is ProfileAction.ShowLanguageBottomSheet -> {
                observeLanguagesJob?.cancel()

                _state.update {
                    it.copy(
                        bottomSheetContent = BottomSheetContent(
                            title = StringResourceId(Res.string.language_change),
                            items = mapLanguageItems(repository.getLanguages().first())
                        ),
                        isBottomSheetVisible = true,
                    )
                }

                observeLanguagesJob = repository.getLanguages()
                    .distinctUntilChanged()
                    .onEach { languages ->
                        _state.update { currentState ->
                            if (currentState.isBottomSheetVisible) {
                                currentState.copy(
                                    bottomSheetContent = currentState.bottomSheetContent?.copy(
                                        items = mapLanguageItems(languages)
                                    )
                                )
                            } else {
                                currentState
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }

            is ProfileAction.ShowThemeBottomSheet -> {
                observeThemesJob?.cancel()

                _state.update {
                    it.copy(
                        bottomSheetContent = BottomSheetContent(
                            title = StringResourceId(Res.string.theme_change),
                            items = mapThemeItems(repository.getThemes().first())
                        ),
                        isBottomSheetVisible = true,
                    )
                }

                observeThemesJob = repository.getThemes()
                    .distinctUntilChanged()
                    .onEach { themes ->
                        _state.update { currentState ->
                            if (currentState.isBottomSheetVisible) {
                                currentState.copy(
                                    bottomSheetContent = currentState.bottomSheetContent?.copy(
                                        items = mapThemeItems(themes)
                                    )
                                )
                            } else {
                                currentState
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }

            ProfileAction.HideBottomSheet -> {
                observeThemesJob?.cancel()
                observeLanguagesJob?.cancel()

                _state.update {
                    it.copy(
                        isBottomSheetVisible = false
                    )
                }
            }

            ProfileAction.ExitApp -> {
                _state.update {
                    it.copy(
                        isRefreshing = true
                    )
                }
                repository.logout()
                    .onSuccess {
                        _state.update {
                            it.copy(
                                isRefreshing = false
                            )
                        }
                    }
                    .onError {
                        _state.update {
                            it.copy(
                                isRefreshing = false
                            )
                        }
                    }
            }
        }
    }

    private fun mapLanguageItems(languages: List<Pair<Language, Boolean>>): List<BottomSheetItem> {
        return languages.map { language ->
            BottomSheetItem(
                text = language.first.toUiText(),
                isSelected = language.second,
                onClick = {
                    onAction(ProfileAction.ChangeLanguage(language.first))
                }
            )
        }
    }

    private fun mapThemeItems(themes: List<Pair<Theme, Boolean>>): List<BottomSheetItem> {
        return themes.map { theme ->
            BottomSheetItem(
                text = theme.first.toUiText(),
                isSelected = theme.second,
                onClick = {
                    onAction(ProfileAction.ChangeTheme(theme.first))
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        observeProfileItemsJob?.cancel()
        observeUserInfoJob?.cancel()
        observeThemesJob?.cancel()
        observeLanguagesJob?.cancel()
        actualizeJob?.cancel()
    }
}