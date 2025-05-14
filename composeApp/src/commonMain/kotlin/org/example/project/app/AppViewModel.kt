package org.example.project.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.example.project.app.domain.Localization
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.datastore.getLanguage
import org.example.project.core.data.datastore.getTheme
import org.example.project.core.data.model.Theme

class AppViewModel(
    private val dataStorePreferences: DataStorePreferences,
    private val localization: Localization
) : ViewModel() {

    private var observeLanguageJob: Job? = null

    init {
        observeLanguage()
    }

    val theme: StateFlow<Theme> = dataStorePreferences.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Theme.SYSTEM
        )

    fun observeLanguage() {
        observeLanguageJob?.cancel()
        observeLanguageJob = dataStorePreferences
            .getLanguage()
            .distinctUntilChanged()
            .onEach { language ->
                localization.applyLanguage(language.toIso())
            }
            .launchIn(viewModelScope)
    }
}

@Composable
fun rememberAppTheme(viewModel: AppViewModel): Boolean {
    val theme by viewModel.theme.collectAsState()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    return remember(theme, isSystemInDarkTheme) {
        when (theme) {
            Theme.DARK -> true
            Theme.LIGHT -> false
            Theme.SYSTEM -> isSystemInDarkTheme
        }
    }
}
