package org.example.project.features.tasks.presentation.task_screen

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
import org.example.project.features.tasks.domain.TasksRepository
import org.example.project.features.tasks.navigation.TasksRoute

class TaskScreenViewModel(
    private val repository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val taskId = savedStateHandle.toRoute<TasksRoute.TaskScreen>().taskId

    private val _state = MutableStateFlow(TaskScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<TaskScreenNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null

    init {
        observerTask()
        actualizeTask()
    }

    private fun actualizeTask() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }
            delay(1000L)
            repository.actualizeTask(taskId)
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                    observerTask()
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

    private fun observerTask() {
        observeJob?.cancel()
        observeJob = repository
            .getTask(taskId)
            .distinctUntilChanged()
            .onEach { task ->
                _state.update {
                    it.copy(
                        taskItem = task
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TaskScreenAction) = viewModelScope.launch {
        when (action) {
            is TaskScreenAction.OnBackClick -> {
                _navigationEvents.emit(
                    TaskScreenNavigationEvent.NavigateBack
                )
            }
            is TaskScreenAction.OnPullToRefresh -> {
                actualizeTask()
            }

            is TaskScreenAction.OpenAttachment -> {
                _navigationEvents.emit(
                    TaskScreenNavigationEvent.OpenFile(action.url)
                )
            }
        }
    }
}