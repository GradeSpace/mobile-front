package org.example.project.features.lessons.presentation.lesson

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
import org.example.project.features.lessons.domain.LessonRepository
import org.example.project.features.lessons.navigation.LessonRoutes

class LessonScreenViewModel(
    private val repository: LessonRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lessonId = savedStateHandle.toRoute<LessonRoutes.Lesson>().eventId

    private val _state = MutableStateFlow(LessonScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<LessonScreenNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeLesson()
        actualizeLesson()
    }

    private fun actualizeLesson() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(1000)
            repository.actualizeLesson(lessonId)
                .onSuccess {
                    observeLesson()
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

    private fun observeLesson() {
        observeJob?.cancel()
        observeJob = repository
            .getLesson(lessonId)
            .distinctUntilChanged()
            .onEach { lesson ->
                _state.update {
                    it.copy(
                        lessonEventItem = lesson
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LessonScreenAction) = viewModelScope.launch {
        when (action) {
            is LessonScreenAction.OnBackClick -> {
                _navigationEvents.emit(
                    LessonScreenNavigationEvent.NavigateBack
                )
            }

            LessonScreenAction.OnPullToRefresh -> {
                actualizeLesson()
            }

            LessonScreenAction.OpenLink -> {
                _state.value.lessonEventItem?.location?.lessonUrl?.let { url ->
                    _navigationEvents.emit(
                        LessonScreenNavigationEvent.OpenLink(url)
                    )
                }
            }
        }
    }
}