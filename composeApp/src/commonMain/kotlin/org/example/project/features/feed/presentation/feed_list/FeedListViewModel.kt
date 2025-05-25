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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.today
import mobile_front.composeapp.generated.resources.yesterday
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.data.utils.getMonthResourceName
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.UiText
import org.example.project.core.presentation.toUiText
import org.example.project.features.feed.domain.FeedEventItem
import org.example.project.features.feed.domain.FeedEventsBlock
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

    private var userRoleJob: Job? = null

    init {
        observeFeedList()
        actualizeFeedList()
        checkUserRole()
    }

    private fun checkUserRole() {
        userRoleJob?.cancel()
        userRoleJob = viewModelScope.launch {
            val currentUser = repository.getCurrentUser()
            _state.update {
                it.copy(
                    hasCreateButton = currentUser?.role == UserRole.Teacher
                )
            }
        }
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
            .map { events ->
                // Преобразуем список событий в блоки
                createFeedBlocks(events)
            }
            .onEach { blocks ->
                _state.update {
                    it.copy(
                        feedBlocks = blocks,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun createFeedBlocks(events: List<FeedEventItem>): List<FeedEventsBlock> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val yesterdayDate = currentDate.minus(1, DateTimeUnit.DAY)

        val groupedEvents = events.groupBy { event ->
            val eventDate = event.lastUpdateDateTime.date
            when (eventDate) {
                currentDate -> "today"
                yesterdayDate -> "yesterday"
                else -> "${eventDate.month}_${eventDate.year}"
            }
        }

        val blocks = mutableListOf<FeedEventsBlock>()
        var blockId = 0

        // Создаем блок "Сегодня"
        groupedEvents["today"]?.let { todayEvents ->
            if (todayEvents.isNotEmpty()) {
                blocks.add(
                    FeedEventsBlock(
                        id = blockId++,
                        title = listOf(UiText.StringResourceId(Res.string.today)),
                        events = todayEvents.sortedByDescending { it.lastUpdateDateTime },
                        blockType = FeedEventsBlock.BlockType.DEFAULT
                    )
                )
            }
        }

        // Создаем блок "Вчера"
        groupedEvents["yesterday"]?.let { yesterdayEvents ->
            if (yesterdayEvents.isNotEmpty()) {
                blocks.add(
                    FeedEventsBlock(
                        id = blockId++,
                        title = listOf(UiText.StringResourceId(Res.string.yesterday)),
                        events = yesterdayEvents.sortedByDescending { it.lastUpdateDateTime },
                        blockType = FeedEventsBlock.BlockType.DEFAULT
                    )
                )
            }
        }

        // Создаем блоки по месяцам
        groupedEvents.entries
            .filter { it.key != "today" && it.key != "yesterday" }
            .groupBy {
                val (month, year) = it.key.split("_")
                "$month $year"
            }
            .forEach { (monthYear, entries) ->
                val monthEvents = entries.flatMap { it.value }
                val (monthStr, yearStr) = monthYear.split(" ")
                val month = kotlinx.datetime.Month.valueOf(monthStr.uppercase())
                val year = yearStr.toInt()

                val monthResource = getMonthResourceName(month)!!
                val titleList = listOf(
                    UiText.StringResourceId(monthResource),
                    UiText.DynamicString("$year")
                )

                blocks.add(
                    FeedEventsBlock(
                        id = blockId++,
                        title = titleList,
                        events = monthEvents.sortedByDescending { it.lastUpdateDateTime },
                        blockType = FeedEventsBlock.BlockType.OLD
                    )
                )
            }

        return blocks
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
