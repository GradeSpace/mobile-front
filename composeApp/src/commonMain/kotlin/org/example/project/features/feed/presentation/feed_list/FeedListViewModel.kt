package org.example.project.features.feed.presentation.feed_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.example.project.features.feed.navigation.FeedRoute.FeedNotification
import org.example.project.features.feed.presentation.feed_list.FeedListNavigationEvent.NavigateTo

class FeedListViewModel(
    private val repository: FeedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FeedListState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<FeedListNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeFeedList()
        actualizeFeedList()
    }

    private fun actualizeFeedList() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(1000L)
            repository.actualizeEvents()
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
            .fetchFeedEvents()
            .distinctUntilChanged()
            .onEach { events ->
                _state.update {
                    it.copy(
                        feedBlocks = events
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: FeedListAction) = viewModelScope.launch {
        when (action) {
            is FeedListAction.FeedListItemClick -> _navigationEvents.emit(
                NavigateTo(
                    FeedNotification(action.item.id)
                )
            )

            FeedListAction.OnPullToRefresh -> {
                actualizeFeedList()
            }

            FeedListAction.CreateNewNotification -> {
                _navigationEvents.emit(NavigateTo(FeedRoute.NotificationCreate))
            }
        }
    }
}