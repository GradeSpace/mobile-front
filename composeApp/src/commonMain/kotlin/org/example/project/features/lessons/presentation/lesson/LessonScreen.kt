package org.example.project.features.lessons.presentation.lesson

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.Platform
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.app.navigation.utils.openFile
import org.example.project.app.navigation.utils.openUrl
import org.example.project.core.domain.EventItemTimeFormat
import org.example.project.core.presentation.ui.common.DesktopRefreshButton
import org.example.project.core.presentation.ui.common.EventAttachments
import org.example.project.core.presentation.ui.common.EventDescription
import org.example.project.core.presentation.ui.common.EventTopBar
import org.example.project.features.feed.presentation.feed_notification.components.EventReceivers
import org.example.project.features.lessons.presentation.lesson.components.LessonLinkBlock
import org.example.project.features.lessons.presentation.lesson.components.LessonStatusBlock
import org.example.project.getPlatform

@Composable
fun LessonScreenRoot(
    viewModel: LessonScreenViewModel,
    navigationManager: NavigationManager
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is LessonScreenNavigationEvent.NavigateBack ->
                    navigationManager.navigateBack()

                is LessonScreenNavigationEvent.OpenLink ->
                    navigationManager.openUrl(navEvent.url)

                is LessonScreenNavigationEvent.OpenFile ->
                    navigationManager.openFile(navEvent.url)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    LessonScreen(state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    state: LessonScreenState,
    onAction: (LessonScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            EventTopBar(
                event = state.lessonEventItem,
                onNavigateBackClick = { onAction(LessonScreenAction.OnBackClick) }
            ) {
                if (getPlatform() == Platform.Desktop) {
                    DesktopRefreshButton { onAction(LessonScreenAction.OnPullToRefresh) }
                } else {
                    Spacer(modifier = Modifier.widthIn(48.dp))
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(LessonScreenAction.OnPullToRefresh) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.lessonEventItem == null) return@PullToRefreshBox
            val scrollState = rememberScrollState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp, top = 8.dp)
            ) {
                EventReceivers(
                    state.lessonEventItem.receivers,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )

                EventDescription(
                    description = state.lessonEventItem.description,
                    lastUpdateTime = state.lessonEventItem.lastUpdateDateTime,
                    timeFormat = EventItemTimeFormat.Lesson(
                        startTime = state.lessonEventItem.startTime,
                        endTime = state.lessonEventItem.endTime,
                        location = state.lessonEventItem.location
                    ),
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )

                state.lessonEventItem.location?.lessonUrl?.let { url ->
                    LessonLinkBlock(
                        url = url,
                        onOpenLink = { onAction(LessonScreenAction.OpenLink) }
                    )
                }

                LessonStatusBlock(
                    lessonStatus = state.lessonEventItem.lessonStatus,
                    attendanceStatus = state.lessonEventItem.attendanceStatus,
                    onClick = { }
                )

                EventAttachments(
                    state.lessonEventItem.attachments,
                    onClick = {
                        onAction(LessonScreenAction.OpenAttachment(it))
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}