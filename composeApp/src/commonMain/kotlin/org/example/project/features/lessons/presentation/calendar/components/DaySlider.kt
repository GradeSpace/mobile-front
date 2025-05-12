package org.example.project.features.lessons.presentation.calendar.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySlider(
    weekPagerState: PagerState,
    onDayClick: (LocalDate) -> Unit,
    typeOfDay: (LocalDate) -> DayType,
    startDate: LocalDate,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = weekPagerState,
        pageSpacing = 7.dp,
        modifier = modifier
            // .padding(top = 16.dp, left = 24.dp, end = 24.dp)
            .fillMaxWidth(),
    ) { index ->
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            val weekStart = startDate.plus(index, DateTimeUnit.WEEK).let {
                it.minus(
                    it.dayOfWeek.ordinal,
                    DateTimeUnit.DAY
                )
            }
            val daysOfWeek = List(7) { dayIndex -> weekStart.plus(dayIndex.toLong(), DateTimeUnit.DAY) }

            items(daysOfWeek.size) { index ->
                Day(
                    day = daysOfWeek[index],
                    dayType = typeOfDay(daysOfWeek[index])
                ) {
                    onDayClick(daysOfWeek[index])
                }
            }
        }
    }
}