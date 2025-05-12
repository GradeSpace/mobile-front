package org.example.project.features.lessons.presentation.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.calendar
import mobile_front.composeapp.generated.resources.i24_today
import mobile_front.composeapp.generated.resources.week_accusative
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.data.utils.weeksUntil
import org.example.project.core.presentation.ui.common.CalendarDialog
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.TitleChip
import org.example.project.features.lessons.presentation.calendar.components.DaySlider
import org.example.project.features.lessons.presentation.calendar.components.DayType
import org.example.project.features.lessons.presentation.calendar.components.LessonSlider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.abs

@Composable
fun CalendarScreenRoot(
    viewModel: CalendarViewModel,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is CalendarScreenNavigationEvent.NavigateTo ->
                    navigationManager.navigateTo(navEvent.route)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    CalendarScreen(
        state,
        viewModel::onAction,
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    state: CalendarScreenState,
    onAction: (CalendarScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isScrollingProgrammatically by remember { mutableStateOf(false) }

    val weekPagerState = rememberPagerState(
        initialPage = state.startDate.weeksUntil(state.selectedDay),
        pageCount = { state.startDate.weeksUntil(state.endDate) }
    )

    val dayPagerState = rememberPagerState(
        initialPage = abs(state.startDate.daysUntil(state.selectedDay)),
        pageCount = { abs(state.startDate.daysUntil(state.endDate)) }
    )

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        CalendarDialog(
            date = state.selectedDay,
            startDate = state.startDate,
            endDate = state.endDate,
            onDateChange = { timeInMillis ->
                if (timeInMillis != null) {
                    onAction(
                        CalendarScreenAction.OnSelectedDayChange(
                            date = Instant
                                .fromEpochMilliseconds(timeInMillis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                        )
                    )
                }
                showDatePicker = false
            },
            onDismissRequest = { showDatePicker = false }
        )
    }

    LaunchedEffect(dayPagerState.currentPage) {
        if (!isScrollingProgrammatically) {
            val newSelectedDay = state.startDate.plus(dayPagerState.currentPage, DateTimeUnit.DAY)
            onAction(CalendarScreenAction.OnSelectedDayChange(newSelectedDay))
        }
        isScrollingProgrammatically = false
    }

    LaunchedEffect(state.selectedDay) {
        val targetDayPage = abs(state.startDate.daysUntil(state.selectedDay))
        if (dayPagerState.currentPage != targetDayPage) {
            isScrollingProgrammatically = true
            dayPagerState.animateScrollToPage(targetDayPage)
        }

        val targetWeekPage = state.startDate.weeksUntil(state.selectedDay)
        if (weekPagerState.currentPage != targetWeekPage) {
            weekPagerState.animateScrollToPage(targetWeekPage)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.calendar),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                showDatePicker = true
                            }
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.i24_today),
                                contentDescription = null
                            )
                        }
                        DesktopRefreshButton {
                            onAction(CalendarScreenAction.OnPullToRefresh)
                        }
                    },
                    modifier = Modifier
                        .shadow(
                            elevation = 1.dp
                        )
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(CalendarScreenAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                stickyHeader(key = "calendar_week_num") {
                    TitleChip(
                        text = "${weekPagerState.currentPage + 1} ${stringResource(Res.string.week_accusative)}",
                        containerColor = DayType.HAS_LESSONS.background,
                        contentColor = DayType.HAS_LESSONS.contentColor,
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp)
                    )
                }

                item {
                    DaySlider(
                        weekPagerState = weekPagerState,
                        onDayClick = { day ->
                            if (day != state.selectedDay)
                                onAction(CalendarScreenAction.OnSelectedDayChange(day))
                        },
                        typeOfDay = { date ->
                            when {
                                date == state.selectedDay -> DayType.SELECTED
                                state.lessonBlocks[date].isNullOrEmpty()
                                    .not() -> DayType.HAS_LESSONS

                                else -> DayType.NOT_SELECTED
                            }
                        },
                        startDate = state.startDate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                item {
                    LessonSlider(
                        dayPagerState = dayPagerState,
                        selectedDayLessons = { page ->
                            state.lessonBlocks[
                                state.startDate.plus(
                                    page,
                                    DateTimeUnit.DAY
                                )
                            ] ?: emptyList()
                        },
                        onLessonClick = { lesson ->
                            onAction(CalendarScreenAction.OnLessonClick(lesson))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                    )
                }
            }

        }
    }
}