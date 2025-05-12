package org.example.project.features.tasks.presentation.tasks_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.completed_tasks
import mobile_front.composeapp.generated.resources.current_tasks
import mobile_front.composeapp.generated.resources.deadline_expired
import mobile_front.composeapp.generated.resources.deadline_not_specified
import mobile_front.composeapp.generated.resources.i24_filters
import mobile_front.composeapp.generated.resources.not_specified
import mobile_front.composeapp.generated.resources.note
import mobile_front.composeapp.generated.resources.reason
import mobile_front.composeapp.generated.resources.submit_before
import mobile_front.composeapp.generated.resources.task_not_issued
import mobile_front.composeapp.generated.resources.task_will_be_issued
import mobile_front.composeapp.generated.resources.tasks
import mobile_front.composeapp.generated.resources.under_check_since
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.data.utils.formatDate
import org.example.project.core.data.utils.formatDateTime
import org.example.project.core.data.utils.formatTime
import org.example.project.core.domain.EventItemTimeFormat
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.EventListItem
import org.example.project.core.presentation.ui.common.TitleChip
import org.example.project.features.tasks.domain.TaskStatus
import org.example.project.features.tasks.domain.TasksEventsBlock.BlockType
import org.example.project.features.tasks.presentation.tasks_list.components.TasksSectionSeparator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TasksListScreenRoot(
    viewModel: TasksListViewModel,
    navigationManager: NavigationManager,
    modifier: Modifier
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is TasksListNavigationEvent.NavigateTo ->
                    navigationManager.navigateTo(navEvent.route)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    TasksListScreen(
        state,
        viewModel::onAction,
        modifier
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    state: TasksListState,
    onAction: (TasksListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.tasks),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onAction(TasksListAction.ToggleFilterMenu) },
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.i24_filters),
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = state.showFiltersMenu,
                        onDismissRequest = { onAction(TasksListAction.ToggleFilterMenu) }
                    ) {
                        BlockType.entries.forEach { blockType ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = state.enabledBlockTypes.contains(blockType),
                                            onCheckedChange = {
                                                onAction(
                                                    TasksListAction.ToggleBlockTypeFilter(
                                                        blockType
                                                    )
                                                )
                                            }
                                        )
                                        Text(
                                            text = blockType.toString(),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                },
                                onClick = {
                                    onAction(TasksListAction.ToggleBlockTypeFilter(blockType))
                                }
                            )
                        }
                    }

                    DesktopRefreshButton {
                        onAction(TasksListAction.OnPullToRefresh)
                    }
                },
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(TasksListAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()


                if (state.tasksBlocks.isNotEmpty() &&
                    state.tasksBlocks[0].blockType != BlockType.COMPLETED_TASKS
                ) {
                    item {
                        TasksSectionSeparator()
                    }

                    item {
                        Text(
                            text = stringResource(Res.string.current_tasks),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth()
                        )
                    }

                    item {
                        Spacer(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                }

                var prevBlockType: BlockType? = null
                state.tasksBlocks.forEach { block ->
                    if (
                        block.blockType == BlockType.COMPLETED_TASKS &&
                        prevBlockType != BlockType.COMPLETED_TASKS
                    ) {
                        item {
                            if (prevBlockType != null) {
                                TasksSectionSeparator()
                            }
                            Text(
                                text = stringResource(Res.string.completed_tasks),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .fillMaxWidth()
                            )
                            Spacer(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                            )
                        }
                    }
                    stickyHeader(key = "tasks_sticky_header_${block.id}") {
                        TitleChip(
                            text = block.title.map { it.asString() }.joinToString(" "),
                            containerColor = getContainerColor(block.blockType),
                            contentColor = getContentColor(block.blockType),
                            modifier = modifier
                                .padding(top = 4.dp)
                                .padding(bottom = 2.dp)
                                .padding(vertical = 4.dp)
                        )
                    }
                    itemsIndexed(
                        items = block.tasks,
                        key = { _, event ->
                            event.id
                        }
                    ) { i, event ->
                        val timeFormat = EventItemTimeFormat.Full
                        EventListItem(
                            eventItem = event,
                            timeFormat = timeFormat,
                            onClick = {
                                onAction(TasksListAction.TasksListItemClick(event))
                            },
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = getContainerColor(block.blockType),
                                contentColor = getContentColor(block.blockType)
                            ),
                            modifier = modifier
                                .padding(bottom = 8.dp)
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            val deadlineInfo = event.deadLine.toDeadlineInfo()
                            when (val status = event.status) {
                                is TaskStatus.Completed -> {
                                    if (event.grade != null) {
                                        Text(
                                            text = buildString {
                                                append("${stringResource(Res.string.note)}: ")
                                                append("${event.grade.currentGrade}")
                                                if (event.grade.maxGrade != null) {
                                                    append(" / ")
                                                    append("${event.grade.maxGrade}")
                                                }
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }

                                is TaskStatus.Rejected -> {
                                    Text(
                                        text = "${stringResource(Res.string.reason)}: ${
                                            status.reason ?: stringResource(
                                                Res.string.not_specified
                                            )
                                        }",
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Text(
                                        text = deadlineInfo.text,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                is TaskStatus.UnderCheck -> {
                                    status.dateTime?.let { startTime ->
                                        Text(
                                            text = "${stringResource(Res.string.under_check_since)} ${startTime.formatDate()} • ${startTime.formatTime()}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }

                                is TaskStatus.Issued -> {
                                    Text(
                                        text = deadlineInfo.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = deadlineInfo.color,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                is TaskStatus.NotIssued -> {
                                    Text(
                                        text = if (status.dateTime != null) {
                                            "${stringResource(Res.string.task_will_be_issued)}: ${status.dateTime.formatDateTime()}"
                                        } else stringResource(Res.string.task_not_issued),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                null -> {}
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    prevBlockType = block.blockType
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
                onClick = { onAction(TasksListAction.CreateNewTask) },
            ) { Icon(Icons.Default.Add, null) }
        }
    }
}

@Composable
private fun LocalDateTime?.toDeadlineInfo(): DeadlineInfo {
    if (this == null) {
        return DeadlineInfo(
            text = buildString {
                append(stringResource(Res.string.submit_before))
                append(": ")
                append(stringResource(Res.string.deadline_not_specified))
            },
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val deadlineColor = when {
        this < now -> MaterialTheme.colorScheme.onErrorContainer

        this.date.minus(now.date).days <= 1 ->
            MaterialTheme.colorScheme.error

        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }
    val deadlineText = when {
        this < now -> stringResource(Res.string.deadline_expired)
        else -> buildString {
            append(stringResource(Res.string.submit_before))
            append(": ")
            append(this@toDeadlineInfo.formatDateTime())
        }
    }

    return DeadlineInfo(
        text = deadlineText,
        color = deadlineColor
    )
}

private data class DeadlineInfo(
    val text: String,
    val color: Color
)

@Composable
private fun getContainerColor(blockType: BlockType): Color {
    return when (blockType) {
        BlockType.COMPLETED_TASKS -> MaterialTheme.colorScheme.secondaryContainer
        BlockType.REJECTED_TASKS -> MaterialTheme.colorScheme.errorContainer
        BlockType.GIVEN_TASKS -> MaterialTheme.colorScheme.primaryContainer
        BlockType.NOT_ISSUED_TASKS -> MaterialTheme.colorScheme.surfaceVariant
        BlockType.UNDER_CHECK -> MaterialTheme.colorScheme.tertiaryContainer
    }
}

@Composable
private fun getContentColor(blockType: BlockType): Color {
    return when (blockType) {
        BlockType.COMPLETED_TASKS -> MaterialTheme.colorScheme.onSecondaryContainer
        BlockType.REJECTED_TASKS -> MaterialTheme.colorScheme.onErrorContainer
        BlockType.GIVEN_TASKS -> MaterialTheme.colorScheme.onPrimaryContainer
        BlockType.NOT_ISSUED_TASKS -> MaterialTheme.colorScheme.onSurfaceVariant
        BlockType.UNDER_CHECK -> MaterialTheme.colorScheme.onTertiaryContainer
    }
}