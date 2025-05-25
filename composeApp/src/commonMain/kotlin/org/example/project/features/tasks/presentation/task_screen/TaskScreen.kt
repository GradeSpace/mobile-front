package org.example.project.features.tasks.presentation.task_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.max
import mobile_front.composeapp.generated.resources.min
import org.example.project.Platform
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.app.navigation.utils.openFile
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.EventAttachments
import org.example.project.core.presentation.ui.common.EventDescription
import org.example.project.core.presentation.ui.common.EventTopBar
import org.example.project.features.feed.presentation.feed_notification.components.EventReceivers
import org.example.project.features.tasks.presentation.task_screen.components.TaskInfoDisplay
import org.example.project.getPlatform
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskRootScreen(
    viewModel: TaskScreenViewModel,
    navigationManager: NavigationManager
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                TaskScreenNavigationEvent.NavigateBack -> navigationManager.navigateBack()
                is TaskScreenNavigationEvent.OpenFile -> navigationManager.openFile(navEvent.url)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    TaskScreen(state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    state: TaskScreenState,
    onAction: (TaskScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            EventTopBar(
                event = state.taskItem,
                onNavigateBackClick = { onAction(TaskScreenAction.OnBackClick) },
                actions = {
                    if (getPlatform() == Platform.Desktop) {
                        DesktopRefreshButton { onAction(TaskScreenAction.OnPullToRefresh) }
                    } else {
                        Spacer(modifier = Modifier.widthIn(48.dp))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(TaskScreenAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.taskItem == null) return@PullToRefreshBox
            val scrollState = rememberScrollState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp, top = 8.dp)
            ) {
                EventReceivers(
                    state.taskItem.receivers,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                EventDescription(
                    description = state.taskItem.description,
                    lastUpdateTime = state.taskItem.lastUpdateDateTime,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                ) {
                    state.taskItem.grade?.let { grade ->
                        Spacer(Modifier.height(8.dp))
                        if (grade.minGrade != null || grade.maxGrade != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                grade.minGrade?.let { minGrade ->
                                    Text(
                                        text = "${stringResource(Res.string.min)}: $minGrade",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                } ?: Spacer(Modifier.weight(1f))

                                grade.maxGrade?.let { maxGrade ->
                                    Text(
                                        text = "${stringResource(Res.string.max)}: $maxGrade",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f)
                                    )
                                } ?: Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                state.taskItem.status?.let { status ->
                    TaskInfoDisplay(
                        status = status,
                        deadline = state.taskItem.deadLine,
                        grade = state.taskItem.grade
                    )
                }
                EventAttachments(
                    state.taskItem.attachments,
                    onClick = {
                        onAction(TaskScreenAction.OpenAttachment(it))
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}