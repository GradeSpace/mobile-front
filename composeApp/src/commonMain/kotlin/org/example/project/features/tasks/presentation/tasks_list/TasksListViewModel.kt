package org.example.project.features.tasks.presentation.tasks_list

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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.toUiText
import org.example.project.features.tasks.domain.TasksRepository
import org.example.project.features.tasks.navigation.TasksRoute.TaskScreen
import org.example.project.features.tasks.presentation.tasks_list.TasksListNavigationEvent.NavigateTo

class TasksListViewModel(
    private val repository: TasksRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TasksListState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<TasksListNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var actualizeJob: Job? = null
    private var observeJob: Job? = null

    init {
        observeTasksList()
        actualizeTasksList()
    }

    private fun actualizeTasksList() {
        actualizeJob?.cancel()
        actualizeJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }
            delay(1000L)
            repository.actualizeTasks()
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                        )
                    }
                    observeTasksList()
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
    private fun observeTasksList() {
        observeJob?.cancel()
        observeJob = _state
            .map { it.enabledBlockTypes }
            .distinctUntilChanged()
            .flatMapLatest { filter ->
                repository.fetchTasksEvents(filter)
            }
            .onEach { filteredBlocks ->
                _state.update { it.copy(tasksBlocks = filteredBlocks) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TasksListAction) = viewModelScope.launch {
        when (action) {
            is TasksListAction.TasksListItemClick -> _navigationEvents.emit(
                NavigateTo(
                    TaskScreen(action.item.id)
                )
            )

            TasksListAction.CreateNewTask -> {
            }

            TasksListAction.ToggleFilterMenu -> {
                _state.update { it.copy(showFiltersMenu = !it.showFiltersMenu) }
            }

            TasksListAction.OnPullToRefresh -> {
                _state.update { it.copy(isRefreshing = true) }
                actualizeTasksList()
                _state.update { it.copy(isRefreshing = false) }
            }

            is TasksListAction.ToggleBlockTypeFilter -> {
                val newSet = _state.value.enabledBlockTypes.toMutableSet().apply {
                    if (contains(action.blockType)) remove(action.blockType)
                    else add(action.blockType)
                }
                _state.update {
                    it.copy(enabledBlockTypes = newSet)
                }
            }
        }
    }
}