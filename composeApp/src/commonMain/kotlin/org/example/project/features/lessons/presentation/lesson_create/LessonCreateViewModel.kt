package org.example.project.features.lessons.presentation.lesson_create

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
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
import org.example.project.core.data.model.event.EventLocation
import org.example.project.core.domain.Result
import org.example.project.core.presentation.AttachmentSource
import org.example.project.core.presentation.UiSnackbar
import org.example.project.core.presentation.UiText.DynamicString
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonEventItem
import org.example.project.features.lessons.domain.LessonRepository
import org.example.project.features.lessons.domain.LessonStatus

class LessonCreateViewModel(
    private val repository: LessonRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LessonCreateState())
    val state = _state.asStateFlow()

    private val _navigationEvents = Channel<LessonCreateNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiChannel = Channel<LessonCreateUiEvent>()
    val uiEvents = _uiChannel.receiveAsFlow()

    private var observeJob: Job? = null
    private var _lessonCreated = false

    init {
        viewModelScope.launch {
            loadDraftData()
            observeReceivers()
        }
    }

    private suspend fun loadDraftData() {
        val lessonCreateDraft = repository.getLessonCreateDraft()

        _state.update {
            it.copy(
                title = lessonCreateDraft.title ?: "",
                subject = lessonCreateDraft.subject ?: "",
                description = lessonCreateDraft.description ?: "",
                pickedReceivers = lessonCreateDraft.receivers ?: emptyList(),
                attachments = lessonCreateDraft.attachments ?: emptyList(),
                lessonDate = lessonCreateDraft.lessonDate,
                startTime = lessonCreateDraft.startTime,
                endTime = lessonCreateDraft.endTime,
                isOnlineLocation = lessonCreateDraft.isOnlineLocationEnabled,
                isOfflineLocation = lessonCreateDraft.isOfflineLocationEnabled,
                onlineLink = lessonCreateDraft.onlineLink,
                offlinePlace = lessonCreateDraft.offlinePlace
            )
        }
    }

    private suspend fun saveCurrentDraft() {
        val currentState = _state.value
        repository.saveLessonDraftTitle(currentState.title)
        repository.saveLessonDraftSubject(currentState.subject)
        repository.saveLessonDraftDescription(currentState.description)
        repository.saveLessonDraftReceivers(currentState.pickedReceivers)
        repository.saveLessonDraftAttachments(currentState.attachments)
        repository.saveLessonDraftDate(currentState.lessonDate)
        repository.saveLessonDraftStartTime(currentState.startTime)
        repository.saveLessonDraftEndTime(currentState.endTime)
        repository.saveLessonDraftOnlineLocationEnabled(currentState.isOnlineLocation)
        repository.saveLessonDraftOfflineLocationEnabled(currentState.isOfflineLocation)
        repository.saveLessonDraftOnlineLink(currentState.onlineLink)
        repository.saveLessonDraftOfflinePlace(currentState.offlinePlace)
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

    fun onAction(action: LessonCreateAction) = viewModelScope.launch {
        when (action) {
            LessonCreateAction.OnBackClick -> {
                saveCurrentDraft()
                _navigationEvents.send(LessonCreateNavigationEvent.NavigateBack)
            }

            is LessonCreateAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description,
                        descriptionError = null
                    )
                }
            }

            is LessonCreateAction.OnReceiverDeselected -> {
                _state.update {
                    it.copy(
                        pickedReceivers = it.pickedReceivers.filter { receiver ->
                            receiver != action.receiver
                        }
                    )
                }
            }

            is LessonCreateAction.OnReceiverSelected -> {
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

            is LessonCreateAction.OnRemoveAttachment -> _state.update {
                it.copy(
                    attachments = it.attachments.filter { attachment ->
                        attachment != action.attachment
                    }
                )
            }

            is LessonCreateAction.OnTitleChange -> {
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

            is LessonCreateAction.OnSubjectChange -> {
                _state.update {
                    it.copy(
                        subject = action.subject,
                        subjectError = if (action.subject.isBlank())
                            DynamicString("Предмет не может быть пустым")
                        else
                            null
                    )
                }
            }

            is LessonCreateAction.OnLessonDateChange -> {
                _state.update {
                    it.copy(
                        lessonDate = action.date
                    )
                }
            }

            is LessonCreateAction.OnStartTimeChange -> {
                _state.update {
                    it.copy(
                        startTime = action.time
                    )
                }
            }

            is LessonCreateAction.OnEndTimeChange -> {
                _state.update {
                    it.copy(
                        endTime = action.time
                    )
                }
            }

            is LessonCreateAction.OnOnlineLocationChange -> {
                _state.update {
                    it.copy(
                        isOnlineLocation = action.isEnabled,
                        locationError = null
                    )
                }
            }

            is LessonCreateAction.OnOfflineLocationChange -> {
                _state.update {
                    it.copy(
                        isOfflineLocation = action.isEnabled,
                        locationError = null
                    )
                }
            }

            is LessonCreateAction.OnOnlineLinkChange -> {
                _state.update {
                    it.copy(
                        onlineLink = action.link,
                        locationError = null
                    )
                }
            }

            is LessonCreateAction.OnOfflinePlaceChange -> {
                _state.update {
                    it.copy(
                        offlinePlace = action.place,
                        locationError = null
                    )
                }
            }

            is LessonCreateAction.OnSourceSelected -> {
                val file = when (action.source) {
                    AttachmentSource.CAMERA -> FileKit.openCameraPicker()
                    AttachmentSource.GALLERY -> FileKit.openFilePicker(type = FileKitType.Image)
                    AttachmentSource.FILE -> FileKit.openFilePicker(type = FileKitType.File())
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

            is LessonCreateAction.OnAttachmentClick -> {
                _navigationEvents.send(LessonCreateNavigationEvent.OpenFile(action.attachment))
            }

            LessonCreateAction.OnSendClick -> {
                val currentState = _state.value

                // Проверяем валидность данных
                val validationErrors = validateLessonData(currentState)

                if (validationErrors.isEmpty()) {
                    // Получаем текущего пользователя
                    val currentUser = repository.currentUser() ?: return@launch

                    val event = LessonEventItem(
                        id = "new_lesson_${System.now()}",
                        title = currentState.title,
                        description = DynamicString(currentState.description),
                        author = currentUser,
                        lastUpdateDateTime = System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                        attachments = currentState.attachments,
                        receivers = currentState.pickedReceivers,
                        location = EventLocation(
                            cabinet = currentState.offlinePlace.takeIf { it.isNotBlank() },
                            lessonUrl = currentState.onlineLink.takeIf { it.isNotBlank() }
                        ),
                        subject = currentState.subject,
                        startTime = currentState.startTime!!,
                        endTime = currentState.endTime,
                        attendanceStatus = AttendanceStatus.NotAttended,
                        lessonStatus = LessonStatus.NotStarted
                    )

                    val result = repository.createLesson(event)

                    if (result is Result.Success) {
                        _lessonCreated = true
                        repository.clearLessonDraft()
                        _navigationEvents.send(LessonCreateNavigationEvent.NavigateBack)
                    } else if (result is Result.Error) {
                        _uiChannel.send(
                            LessonCreateUiEvent.ShowErrorSnackbar(
                                UiSnackbar(
                                    actionLabel = null,
                                    withDismissAction = false,
                                    message = "Не удалось сохранить занятие",
                                    duration = SnackbarDuration.Short
                                )
                            )
                        )
                    }
                } else {
                    // Обновляем состояние с ошибками валидации
                    _state.update { state ->
                        state.copy(
                            titleError = validationErrors["title"]?.let(::DynamicString),
                            descriptionError = validationErrors["description"]?.let(::DynamicString),
                            subjectError = validationErrors["subject"]?.let(::DynamicString),
                            locationError = validationErrors["location"]?.let(::DynamicString),
                            error = validationErrors["general"]?.let(::DynamicString)
                        )
                    }

                    validationErrors["general"]?.let { error ->
                        _uiChannel.send(
                            LessonCreateUiEvent.ShowErrorSnackbar(
                                UiSnackbar(
                                    actionLabel = null,
                                    withDismissAction = false,
                                    message = error,
                                    duration = SnackbarDuration.Short
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun validateLessonData(state: LessonCreateState): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val currentDateTime = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Проверка заголовка
        if (state.title.isBlank()) {
            errors["title"] = "Заголовок не может быть пустым"
        } else if (state.title.length > 40) {
            errors["title"] = "Заголовок не может быть длиннее 40 символов"
        }

        // Проверка предмета
        if (state.subject.isBlank()) {
            errors["subject"] = "Предмет не может быть пустым"
        } else if (state.subject.length > 40) {
            errors["subject"] = "Название предмета не может быть длиннее 40 символов"
        }

        // Проверка описания
        if (state.description.length > 5000) {
            errors["description"] = "Описание не может быть длиннее 5000 символов"
        }

        // Проверка получателей
        if (state.pickedReceivers.isEmpty()) {
            errors["general"] = "Необходимо выбрать хотя бы одного получателя"
        }

        // Проверка даты и времени
        if (state.lessonDate == null) {
            errors["general"] = "Необходимо указать дату занятия"
        }

        if (state.startTime == null) {
            errors["general"] = "Необходимо указать время начала занятия"
        } else if (state.startTime.compareTo(currentDateTime) < 0) {
            errors["general"] = "Время начала занятия не может быть раньше текущего времени"
        }

        if (state.endTime != null && state.startTime != null) {
            if (state.endTime.compareTo(state.startTime) < 0) {
                errors["general"] = "Время окончания занятия не может быть раньше времени начала"
            }
        }

        // Проверка локации
        if (state.isOnlineLocation && state.onlineLink.isBlank()) {
            errors["location"] = "Необходимо указать ссылку для онлайн занятия"
        }

        if (state.isOfflineLocation && state.offlinePlace.isBlank()) {
            errors["location"] = "Необходимо указать место проведения очного занятия"
        }

        return errors
    }

    override fun onCleared() {
        runBlocking {
            if (!_lessonCreated) {
                saveCurrentDraft()
            }
        }
        super.onCleared()
    }
}
