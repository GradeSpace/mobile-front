package org.example.project.features.lessons.presentation.calendar.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.fri
import mobile_front.composeapp.generated.resources.mon
import mobile_front.composeapp.generated.resources.sat
import mobile_front.composeapp.generated.resources.sun
import mobile_front.composeapp.generated.resources.thu
import mobile_front.composeapp.generated.resources.tue
import mobile_front.composeapp.generated.resources.wed
import org.jetbrains.compose.resources.stringResource

enum class DayType {
    SELECTED, HAS_LESSONS, NOT_SELECTED;

    val background: Color
        @Composable
        get() = when(this) {
            SELECTED -> MaterialTheme.colorScheme.primary
            HAS_LESSONS -> MaterialTheme.colorScheme.primaryContainer
            NOT_SELECTED -> MaterialTheme.colorScheme.surfaceContainerHighest
        }

    val contentColor: Color
        @Composable
        get() = when(this) {
            SELECTED -> MaterialTheme.colorScheme.onPrimary
            HAS_LESSONS -> MaterialTheme.colorScheme.onPrimaryContainer
            NOT_SELECTED -> MaterialTheme.colorScheme.onSurfaceVariant
        }

    val borderColor: Color
        @Composable
        get() = when(this) {
            SELECTED -> MaterialTheme.colorScheme.tertiary
            HAS_LESSONS -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            NOT_SELECTED -> Color.Transparent
        }
}

@Composable
fun Day(day: LocalDate, dayType: DayType, onSelectDay: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = dayType.background,
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColorAnimation"
    )

    val textColor by animateColorAsState(
        targetValue = dayType.contentColor,
        animationSpec = tween(durationMillis = 200),
        label = "textColorAnimation"
    )

    val borderColor = dayType.borderColor

    val shape = RoundedCornerShape(12.dp)

    Surface(
        shape = shape,
        color = backgroundColor,
        border = if (dayType != DayType.NOT_SELECTED) {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = borderColor
            )
        } else null,
        shadowElevation = if (dayType == DayType.SELECTED) 2.dp else 0.dp,
        modifier = Modifier
            .width(40.dp)
            .height(56.dp)
            .clip(shape)
            .clickable(onClick = onSelectDay)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                color = textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            Text(
                text = when (day.dayOfWeek) {
                    DayOfWeek.MONDAY -> stringResource(Res.string.mon)
                    DayOfWeek.TUESDAY -> stringResource(Res.string.tue)
                    DayOfWeek.WEDNESDAY -> stringResource(Res.string.wed)
                    DayOfWeek.THURSDAY -> stringResource(Res.string.thu)
                    DayOfWeek.FRIDAY -> stringResource(Res.string.fri)
                    DayOfWeek.SATURDAY -> stringResource(Res.string.sat)
                    DayOfWeek.SUNDAY -> stringResource(Res.string.sun)
                    else -> ""
                },
                color = textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
