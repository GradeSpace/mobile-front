package org.example.project.features.feed.presentation.notification_create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.core.presentation.UiEventsManager
import org.example.project.core.presentation.ui.common.LimitedTextField
import org.example.project.features.feed.presentation.notification_create.components.AddAttachmentsSectionComponent
import org.example.project.features.feed.presentation.notification_create.components.AttachmentSourceBottomSheet
import org.example.project.features.feed.presentation.notification_create.components.ReceiverSelectorComponent

@Composable
fun NotificationCreateRootScreen(
    viewModel: NotificationCreateViewModel,
    navigationManager: NavigationManager,
    uiEventsManager: UiEventsManager
) {
    val navigationEvents = viewModel.navigationEvents
    navigationManager.subscribeNavigationOnLifecycle {
        navigationEvents.collect { navEvent ->
            when (navEvent) {
                is NotificationCreateNavigationEvent.NavigateBack ->
                    navigationManager.navigateBack()
            }
        }
    }

    val uiEvents = viewModel.uiEvents
    uiEventsManager.subscribeEventsOnLifecycle {
        uiEvents.collect { uiEvent ->
            when (uiEvent) {
                is NotificationCreateUiEvent.ShowErrorSnackbar -> uiEventsManager.showSnackBar(
                    uiEvent.snackbar
                )
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    NotificationCreateScreen(state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCreateScreen(
    state: NotificationCreateState,
    onAction: (NotificationCreateAction) -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val keyboardActions = KeyboardActions(
        onDone = {
            keyboardController?.hide()
        }
    )

    var isAttachmentBottomSheetVisible by remember {
        mutableStateOf(false)
    }

    AttachmentSourceBottomSheet(
        isVisible = isAttachmentBottomSheetVisible,
        onDismiss = { isAttachmentBottomSheetVisible = false }
    ) { source ->
        onAction(NotificationCreateAction.OnSourceSelected(source))
        isAttachmentBottomSheetVisible = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создание уведомления") },
                navigationIcon = {
                    IconButton(onClick = { onAction(NotificationCreateAction.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(NotificationCreateAction.OnSendClick) },
                        enabled = state.title.isNotEmpty() && state.titleError == null &&
                            state.descriptionError == null && state.pickedReceivers.isNotEmpty()
                    ) {
                        Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Отправить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Заголовок
            LimitedTextField(
                value = state.title,
                onValueChange = { onAction(NotificationCreateAction.OnTitleChange(it)) },
                label = "Заголовок",
                placeholder = "Введите заголовок уведомления",
                maxLength = 40,
                error = state.titleError,
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = keyboardActions
            )

            // Описание
            LimitedTextField(
                value = state.description,
                onValueChange = { onAction(NotificationCreateAction.OnDescriptionChange(it)) },
                label = "Описание",
                placeholder = "Введите текст уведомления",
                maxLength = 5000,
                error = state.descriptionError,
                maxLines = 10,
                modifier = Modifier.height(120.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = keyboardActions
            )

            ReceiverSelectorComponent(
                availableReceivers = state.availableReceivers,
                selectedReceivers = state.pickedReceivers,
                onReceiverSelected = { onAction(NotificationCreateAction.OnReceiverSelected(it)) },
                onReceiverDeselected = { onAction(NotificationCreateAction.OnReceiverDeselected(it)) },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Вложения
            AddAttachmentsSectionComponent(
                attachments = state.attachments,
                onAddAttachment = { isAttachmentBottomSheetVisible = true },
                onRemoveAttachment = { onAction(NotificationCreateAction.OnRemoveAttachment(it)) }
            )

            // Отображение ошибки
            state.error?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it.asString(),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}