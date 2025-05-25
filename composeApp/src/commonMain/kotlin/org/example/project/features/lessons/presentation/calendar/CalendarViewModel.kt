package org.example.project.features.lessons.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.toUiText
import org.example.project.features.lessons.domain.LessonRepository
import org.example.project.features.lessons.navigation.LessonRoutes
import org.example.project.features.lessons.navigation.LessonRoutes.Lesson
import org.example.project.features.lessons.presentation.calendar.CalendarScreenNavigationEvent.NavigateTo

class CalendarViewModel(
    val repository: LessonRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CalendarScreenNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null
    private var userRoleJob: Job? = null

    init {
        observeCalendar()
        actualizeCalendar()
        checkUserRole()
    }

    private fun checkUserRole() {
        userRoleJob?.cancel()
        userRoleJob = viewModelScope.launch {
            val currentUser = repository.currentUser()
            _state.update {
                it.copy(
                    hasCreateButton = currentUser?.role == UserRole.Teacher
                )
            }
        }
    }

    private fun actualizeCalendar() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }
            delay(1000L)
            repository.actualizeLessons()
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                        )
                    }
                    observeCalendar()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCalendar() {
        observeJob?.cancel()
        observeJob = repository
            .fetchLessonEvents()
            .distinctUntilChanged()
            .onEach { events ->
                _state.update {
                    it.copy(
                        lessonBlocks = events
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CalendarScreenAction) = viewModelScope.launch {
        when (action) {
            is CalendarScreenAction.OnLessonClick -> {
                _navigationEvents.emit(
                    NavigateTo(
                        Lesson(
                            action.lesson.id
                        )
                    )
                )
            }

            is CalendarScreenAction.OnSelectedDayChange -> {
                _state.update {
                    it.copy(
                        selectedDay = action.date
                    )
                }
            }

            CalendarScreenAction.OnPullToRefresh -> {
                actualizeCalendar()
            }

            CalendarScreenAction.CreateNewLesson -> {
                if (_state.value.hasCreateButton) {
                    _navigationEvents.emit(NavigateTo(LessonRoutes.LessonCreate))
                }
            }
        }
    }
}
