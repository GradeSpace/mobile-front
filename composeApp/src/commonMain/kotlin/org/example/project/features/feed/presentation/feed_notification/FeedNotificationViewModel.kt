package org.example.project.features.feed.presentation.feed_notification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.toUiText
import org.example.project.features.feed.domain.FeedRepository
import org.example.project.features.feed.navigation.FeedRoute

class FeedNotificationViewModel(
    private val repository: FeedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val notificationId = savedStateHandle.toRoute<FeedRoute.FeedNotification>().eventId

    private val _state = MutableStateFlow(FeedNotificationState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<FeedNotificationNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeFeedList()
        actualizeNotification()
    }

    private fun actualizeNotification() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(1000)
            repository.actualizeEvent(notificationId)
                .onSuccess {
                    observeFeedList()
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

    private fun observeFeedList() {
        observeJob?.cancel()
        observeJob = repository
            .getEvent(notificationId)
            .distinctUntilChanged()
            .onEach { notification ->
                _state.update {
                    it.copy(
                        notificationItem = notification
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: FeedNotificationAction) = viewModelScope.launch {
        when (action) {
            is FeedNotificationAction.OnBackClick -> {
                _navigationEvents.emit(
                    FeedNotificationNavigationEvent.NavigateBack
                )
            }

            FeedNotificationAction.OnPullToRefresh -> {
                actualizeNotification()
            }
        }
    }
}