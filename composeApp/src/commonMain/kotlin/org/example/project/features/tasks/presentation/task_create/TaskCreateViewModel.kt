package org.example.project.features.tasks.presentation.task_create

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
import org.example.project.features.tasks.domain.TaskEventItem
import org.example.project.features.tasks.domain.TaskStatus
import org.example.project.features.tasks.domain.TasksRepository
import org.example.project.features.tasks.domain.VariantDistributionMode

class TaskCreateViewModel(
    private val repository: TasksRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TaskCreateState())
    val state = _state.asStateFlow()

    private val _navigationEvents = Channel<TaskCreateNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiChannel = Channel<TaskCreateUiEvent>()
    val uiEvents = _uiChannel.receiveAsFlow()

    private var observeJob: Job? = null
    private var _taskCreated = false

    init {
        viewModelScope.launch {
            loadDraftData()
            observeReceivers()
        }
    }

    private suspend fun loadDraftData() {
        val taskCreateDraft = repository.getCreateTaskDraft()

        _state.update {
            it.copy(
                title = taskCreateDraft.title ?: "",
                description = taskCreateDraft.description ?: "",
                pickedReceivers = taskCreateDraft.receivers ?: emptyList(),
                attachments = taskCreateDraft.attachments ?: emptyList(),
                gradeRange = taskCreateDraft.gradeRange,
                variantDistributionMode = taskCreateDraft.variantDistributionMode,
                variants = taskCreateDraft.variants
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

    fun onAction(action: TasksCreateAction) = viewModelScope.launch {
        when (action) {
            TasksCreateAction.OnBackClick -> {
                saveCurrentDraft()
                _navigationEvents.send(TaskCreateNavigationEvent.NavigateBack)
            }

            is TasksCreateAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description,
                        descriptionError = null
                    )
                }
            }

            is TasksCreateAction.OnReceiverDeselected -> {
                _state.update {
                    it.copy(
                        pickedReceivers = it.pickedReceivers.filter { receiver ->
                            receiver != action.receiver
                        }
                    )
                }
            }

            is TasksCreateAction.OnReceiverSelected -> {
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

            is TasksCreateAction.OnRemoveAttachment -> _state.update {
                it.copy(
                    attachments = it.attachments.filter { attachment ->
                        attachment != action.attachment
                    }
                )
            }

            is TasksCreateAction.OnGradeRangeChange -> {
                _state.update {
                    it.copy(gradeRange = action.gradeRange)
                }
                viewModelScope.launch {
                    if (action.gradeRange != null) {
                        repository.saveTaskGradeRange(action.gradeRange)
                    }
                }
            }

            is TasksCreateAction.OnDeadlineChange -> {
                _state.update {
                    it.copy(deadline = action.deadline)
                }
            }

            is TasksCreateAction.OnIssueTimeChange -> {
                _state.update {
                    it.copy(issueTime = action.issueTime)
                }
            }

            is TasksCreateAction.OnVariantDistributionModeChange -> {
                _state.update {
                    it.copy(variantDistributionMode = action.mode)
                }
                viewModelScope.launch {
                    repository.saveTaskVariantDistributionMode(action.mode)
                }
            }

            is TasksCreateAction.OnVariantsChange -> {
                _state.update {
                    it.copy(variants = action.variants)
                }
                viewModelScope.launch {
                    if (action.variants != null) {
                        repository.saveVariants(action.variants)
                    }
                }
            }

            TasksCreateAction.OnSendClick -> {
                val currentState = _state.value

                // Проверяем валидность данных
                val validationErrors = validateTaskData(currentState)

                if (validationErrors.isEmpty()) {
                    // Получаем текущего пользователя
                    val currentUser = repository.currentUser() ?: return@launch

                    val event = TaskEventItem(
                        id = "new_event_${System.now()}",
                        title = currentState.title,
                        description = DynamicString(currentState.description),
                        author = currentUser,
                        lastUpdateDateTime = System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                        attachments = currentState.attachments,
                        receivers = currentState.pickedReceivers,
                        grade = currentState.gradeRange,
                        deadLine = currentState.deadline,
                        status = TaskStatus.NotIssued(
                            dateTime = currentState.issueTime
                        )
                    )

                    val result = repository.createTask(event)

                    if (result is Result.Success) {
                        _taskCreated = true
                        repository.clearNotificationDraft()
                        _navigationEvents.send(TaskCreateNavigationEvent.NavigateBack)
                    } else if (result is Result.Error) {
                        _uiChannel.send(
                            TaskCreateUiEvent.ShowErrorSnackbar(
                                UiSnackbar(
                                    actionLabel = null,
                                    withDismissAction = false,
                                    message = "Не удалось сохранить",
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
                            error = validationErrors["general"]?.let(::DynamicString)
                        )
                    }

                    validationErrors["general"]?.let { error ->
                        _uiChannel.send(
                            TaskCreateUiEvent.ShowErrorSnackbar(
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

            is TasksCreateAction.OnTitleChange -> {
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

            is TasksCreateAction.OnSourceSelected -> {
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

            is TasksCreateAction.OnAttachmentClick -> {
                _navigationEvents.send(TaskCreateNavigationEvent.OpenFile(action.attachment))
            }
        }
    }

    private suspend fun saveCurrentDraft() {
        val currentState = _state.value
        repository.saveTaskDraftTitle(currentState.title)
        repository.saveTaskDraftDescription(currentState.description)
        repository.saveTaskDraftReceivers(currentState.pickedReceivers)
        repository.saveTaskDraftAttachments(currentState.attachments)

        if (currentState.gradeRange != null) {
            repository.saveTaskGradeRange(currentState.gradeRange)
        }
        repository.saveTaskVariantDistributionMode(currentState.variantDistributionMode)
        if (currentState.variants != null) {
            repository.saveVariants(currentState.variants)
        }
    }

    private fun validateTaskData(state: TaskCreateState): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val currentDateTime = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Проверка заголовка
        if (state.title.isBlank()) {
            errors["title"] = "Заголовок не может быть пустым"
        } else if (state.title.length > 40) {
            errors["title"] = "Заголовок не может быть длиннее 40 символов"
        }

        // Проверка описания
        if (state.description.length > 5000) {
            errors["description"] = "Описание не может быть длиннее 5000 символов"
        }

        // Проверка получателей
        if (state.pickedReceivers.isEmpty()) {
            errors["general"] = "Необходимо выбрать хотя бы одного получателя"
        }

        // Проверка времени выдачи
        if (state.issueTime != null) {
            if (state.issueTime.compareTo(currentDateTime) < 0) {
                errors["general"] = "Время выдачи не может быть раньше текущего времени"
            }
        }

        // Проверка дедлайна
        if (state.deadline != null) {
            // Проверка, что дедлайн не раньше текущего времени
            if (state.deadline.compareTo(currentDateTime) < 0) {
                errors["general"] = "Дедлайн не может быть раньше текущего времени"
            }

            // Проверка, что дедлайн не раньше времени выдачи (если время выдачи задано)
            if (state.issueTime != null && state.deadline.compareTo(state.issueTime) < 0) {
                errors["general"] = "Дедлайн не может быть раньше времени выдачи"
            }
        }

        // Проверка оценок
        if (state.gradeRange != null) {
            val minGrade = state.gradeRange.minGrade?.value
            val maxGrade = state.gradeRange.maxGrade?.value

            if (maxGrade == null) {
                errors["general"] = "Необходимо указать максимальную оценку"
            } else if (minGrade != null && minGrade > maxGrade) {
                errors["general"] = "Минимальная оценка не может быть больше максимальной"
            }
        }

        // Проверка вариантов
        if (state.variantDistributionMode != VariantDistributionMode.NONE && state.variants != null) {
            // Проверяем, что все варианты имеют текст
            val emptyVariants = state.variants.filter { it.text.isNullOrBlank() }
            if (emptyVariants.isNotEmpty()) {
                errors["general"] = "Все варианты должны содержать текст"
            }

            // Проверяем длину текста вариантов
            val longVariants = state.variants.filter { (it.text?.length ?: 0) > 1000 }
            if (longVariants.isNotEmpty()) {
                errors["general"] = "Текст варианта не может быть длиннее 1000 символов"
            }

            // Если выбран режим распределения по получателям, проверяем, что все варианты имеют получателей
            if (state.variantDistributionMode == VariantDistributionMode.VAR_TO_RECEIVER) {
                val variantsWithoutReceivers = state.variants.filter { it.receivers.isNullOrEmpty() }
                if (variantsWithoutReceivers.isNotEmpty()) {
                    errors["general"] = "Все варианты должны иметь назначенных получателей"
                }

                // Проверяем, что все получатели назначены на варианты
                val assignedReceivers = state.variants.flatMap { it.receivers ?: emptyList() }
                val unassignedReceivers = state.pickedReceivers.filter { it !in assignedReceivers }
                if (unassignedReceivers.isNotEmpty()) {
                    errors["general"] = "Все получатели должны быть назначены на варианты"
                }
            }
        }

        return errors
    }

    override fun onCleared() {
        runBlocking {
            if (!_taskCreated) {
                saveCurrentDraft()
            }
        }
        super.onCleared()
    }
}
