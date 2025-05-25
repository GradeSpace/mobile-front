package org.example.project.features.feed.presentation.notification_create

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.FileKitType.File
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.app.navigation.launchers.openCameraPicker
import org.example.project.core.data.model.attachment.Attachment.FileAttachment
import org.example.project.core.data.model.attachment.toFileType
import org.example.project.core.domain.Result
import org.example.project.core.presentation.AttachmentSource
import org.example.project.core.presentation.UiSnackbar
import org.example.project.core.presentation.UiText.DynamicString
import org.example.project.features.feed.domain.FeedEventItem
import org.example.project.features.feed.domain.FeedRepository
import org.example.project.features.feed.presentation.notification_create.NotificationCreateUiEvent.ShowErrorSnackbar

class NotificationCreateViewModel(
    private val repository: FeedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationCreateState())
    val state = _state.asStateFlow()

    private val _navigationEvents = Channel<NotificationCreateNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiChannel = Channel<NotificationCreateUiEvent>()
    val uiEvents = _uiChannel.receiveAsFlow()

    private var observeJob: Job? = null
    private var _notificationSent = false

    init {
        viewModelScope.launch {
            loadDraftData()
            observeReceivers()
        }
    }

    private suspend fun loadDraftData() {
        val title = repository.getNotificationDraftTitle()
        val description = repository.getNotificationDraftDescription()
        val receivers = repository.getNotificationDraftReceivers()
        val attachments = repository.getNotificationDraftAttachments()

        _state.update {
            it.copy(
                title = title,
                description = description,
                pickedReceivers = receivers,
                attachments = attachments
            )
        }
    }

    private fun observeReceivers() {
        observeJob?.cancel()
        observeJob = repository
            .fetchReceivers()
            .distinctUntilChanged()
            .onEach { receivers ->
                _state.update {
                    it.copy(
                        availableReceivers = receivers
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NotificationCreateAction) = viewModelScope.launch {
        when (action) {
            NotificationCreateAction.OnBackClick -> {
                saveCurrentDraft()
                _navigationEvents.send(NotificationCreateNavigationEvent.NavigateBack)
            }

            is NotificationCreateAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description,
                        descriptionError = null
                    )
                }
            }

            is NotificationCreateAction.OnReceiverDeselected -> {
                _state.update {
                    it.copy(
                        pickedReceivers = it.pickedReceivers.filter { receiver ->
                            receiver != action.receiver
                        }
                    )
                }
            }

            is NotificationCreateAction.OnReceiverSelected -> {
                _state.update {
                    if (it.pickedReceivers.size < 10) {
                        it.copy(
                            pickedReceivers = it.pickedReceivers + action.receiver
                        )
                    } else {
                        it
                    }
                }
            }

            is NotificationCreateAction.OnRemoveAttachment -> _state.update {
                it.copy(
                    attachments = it.attachments.filter { attachment ->
                        attachment != action.attachment
                    }
                )
            }

            NotificationCreateAction.OnSendClick -> {
                val currentState = _state.value
                val currentUser = repository.getCurrentUser() ?: return@launch
                val event = FeedEventItem(
                    id = "new_event_${System.now()}",
                    title = currentState.title,
                    description = DynamicString(currentState.description),
                    author = currentUser,
                    lastUpdateDateTime = System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                    attachments = currentState.attachments,
                    receivers = currentState.pickedReceivers
                )

                val result = repository.createEvent(event)

                if (result is Result.Success) {
                    _notificationSent = true
                    repository.clearNotificationDraft()
                    _navigationEvents.send(NotificationCreateNavigationEvent.NavigateBack)
                } else if (result is Result.Error) {
                    _uiChannel.send(
                        ShowErrorSnackbar(
                            UiSnackbar(
                                actionLabel = null,
                                withDismissAction = false,
                                message = "Не удалось сохранить",
                                duration = SnackbarDuration.Short
                            )
                        )
                    )
                }
            }

            is NotificationCreateAction.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = action.title,
                        titleError = if (action.title.isBlank())
                            DynamicString("Заголовок не может быть пустым")
                        else
                            null
                    )
                }
            }

            is NotificationCreateAction.OnSourceSelected -> {
                val file = when (action.source) {
                    AttachmentSource.CAMERA -> FileKit.openCameraPicker()
                    AttachmentSource.GALLERY -> FileKit.openFilePicker(type = FileKitType.Image)
                    AttachmentSource.FILE -> FileKit.openFilePicker(type = File())
                }
                if (file != null) {
                    _state.update {
                        it.copy(
                            attachments = it.attachments + FileAttachment(
                                id = it.attachments.size.toString(),
                                url = file.path,
                                fileName = file.name,
                                fileSize = file.size(),
                                fileType = file.extension.toFileType()
                            )
                        )
                    }
                }
            }

            is NotificationCreateAction.OnAttachmentClick -> {
                _navigationEvents.send(NotificationCreateNavigationEvent.OpenFile(action.attachment))
            }
        }
    }

    private suspend fun saveCurrentDraft() {
        val currentState = _state.value
        repository.saveNotificationDraftTitle(currentState.title)
        repository.saveNotificationDraftDescription(currentState.description)
        repository.saveNotificationDraftReceivers(currentState.pickedReceivers)
        repository.saveNotificationDraftAttachments(currentState.attachments)
    }

    override fun onCleared() {
        runBlocking {
            if (!_notificationSent) {
                saveCurrentDraft()
            }
        }
        super.onCleared()
    }
}