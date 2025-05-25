package org.example.project.features.tasks.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.given_tasks
import mobile_front.composeapp.generated.resources.no_description
import mobile_front.composeapp.generated.resources.not_given_tasks
import mobile_front.composeapp.generated.resources.rejected_tasks
import mobile_front.composeapp.generated.resources.today
import mobile_front.composeapp.generated.resources.under_check
import mobile_front.composeapp.generated.resources.yesterday
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.Grade
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.data.model.user.User
import org.example.project.core.data.utils.getMonthResourceName
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.repository.UserRepository
import org.example.project.core.presentation.UiText
import org.example.project.core.presentation.asList
import org.example.project.features.feed.data.mock.FeedTextMock
import org.example.project.features.feed.domain.FeedAction
import org.example.project.features.tasks.data.clearTaskDraft
import org.example.project.features.tasks.data.getTaskCreateDraft
import org.example.project.features.tasks.data.saveTaskDraftAttachments
import org.example.project.features.tasks.data.saveTaskDraftDescription
import org.example.project.features.tasks.data.saveTaskDraftGradeRange
import org.example.project.features.tasks.data.saveTaskDraftReceivers
import org.example.project.features.tasks.data.saveTaskDraftTitle
import org.example.project.features.tasks.data.saveTaskDraftVariantDistributionMode
import org.example.project.features.tasks.data.saveTaskDraftVariants
import org.example.project.features.tasks.domain.TaskCreateDraft
import org.example.project.features.tasks.domain.TaskEventItem
import org.example.project.features.tasks.domain.TaskStatus
import org.example.project.features.tasks.domain.TaskVariant
import org.example.project.features.tasks.domain.TasksEventsBlock
import org.example.project.features.tasks.domain.TasksEventsBlock.BlockType
import org.example.project.features.tasks.domain.TasksRepository
import org.example.project.features.tasks.domain.VariantDistributionMode
import kotlin.random.Random

class TasksMockRepository(
    private val dataStorePreferences: DataStorePreferences,
    private val userRepository: UserRepository
) : TasksRepository {

    private val localTasks = mutableListOf<TaskEventItem>()
    val localTasksBlocks = mutableListOf<TasksEventsBlock>()

    init {
        init()
    }

    override suspend fun getCreateTaskDraft(): TaskCreateDraft {
        return dataStorePreferences.getTaskCreateDraft()
    }

    override suspend fun saveTaskDraftTitle(title: String) {
        dataStorePreferences.saveTaskDraftTitle(title)
    }

    override suspend fun saveTaskDraftDescription(description: String) {
        dataStorePreferences.saveTaskDraftDescription(description)
    }

    override suspend fun saveTaskDraftReceivers(receivers: List<String>) {
        dataStorePreferences.saveTaskDraftReceivers(receivers)
    }

    override suspend fun saveTaskDraftAttachments(attachments: List<Attachment>) {
        dataStorePreferences.saveTaskDraftAttachments(attachments)
    }

    override suspend fun saveTaskGradeRange(gradeRange: GradeRange) {
        dataStorePreferences.saveTaskDraftGradeRange(gradeRange)
    }

    override suspend fun saveTaskVariantDistributionMode(mode: VariantDistributionMode) {
        dataStorePreferences.saveTaskDraftVariantDistributionMode(mode)
    }

    override suspend fun saveVariants(variants: List<TaskVariant>) {
        dataStorePreferences.saveTaskDraftVariants(variants)
    }

    override suspend fun clearNotificationDraft() {
        dataStorePreferences.clearTaskDraft()
    }

    override suspend fun currentUser(): User? {
        return userRepository.getCurrentUser()
    }

    private fun init() {
        val mockUser = User(
            uid = "teacher1",
            name = "Петр",
            surname = "Петров",
            middleName = "Петрович"
        )

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        for (daysAgo in 0..49) {
            val eventDate = currentDateTime.date.minus(daysAgo % 10, DateTimeUnit.DAY)

            val eventTime = LocalTime(
                hour = Random.nextInt(8, 20),
                minute = Random.nextInt(0, 60)
            )

            val eventDateTime = LocalDateTime(eventDate, eventTime)

            val deadlineDateTime = LocalDateTime(
                eventDateTime.date.plus(Random.nextInt(0, 10), DateTimeUnit.DAY),
                eventDateTime.time
            )

            val tasks = (1..2).map { int ->
                // Создаем список действий для заданий
                val actionsList = mutableListOf<FeedAction>()

                // Определяем статус задания
                val taskStatus = when {
                    daysAgo % 5 == 0 -> TaskStatus.Completed(eventDateTime)
                    daysAgo % 4 == 0 -> TaskStatus.Rejected(
                        eventDateTime,
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer"
                    )

                    daysAgo % 3 == 0 -> TaskStatus.UnderCheck(eventDateTime)
                    daysAgo % 2 == 0 -> TaskStatus.Issued(eventDateTime)
                    else -> TaskStatus.NotIssued(eventDateTime)
                }

                // Добавляем действия в зависимости от статуса
                when (taskStatus) {
                    is TaskStatus.NotIssued -> {
                        actionsList.add(
                            FeedAction.ButtonAction(
                                actionName = "Получить задание",
                                action = { }
                            )
                        )
                    }

                    is TaskStatus.Issued -> {
                        actionsList.add(
                            FeedAction.ButtonAction(
                                actionName = "Сдать на проверку",
                                action = { }
                            )
                        )
                    }

                    is TaskStatus.UnderCheck -> {
                        actionsList.add(
                            FeedAction.PerformedAction(
                                title = "Задание на проверке"
                            )
                        )
                    }

                    is TaskStatus.Rejected -> {
                        actionsList.add(
                            FeedAction.PerformedAction(
                                title = "Задание отклонено"
                            )
                        )
                        actionsList.add(
                            FeedAction.ButtonAction(
                                actionName = "Сдать повторно",
                                action = { }
                            )
                        )
                    }

                    is TaskStatus.Completed -> {
                        actionsList.add(
                            FeedAction.PerformedAction(
                                title = "Задание выполнено"
                            )
                        )
                    }
                }

                // Создаем оценки для заданий
                val grade = if (taskStatus is TaskStatus.Completed) {
                    GradeRange(
                        minGrade = Grade(0.0),
                        maxGrade = Grade(10.0),
                        currentGrade = Grade(Random.nextDouble(5.0, 10.0))
                    )
                } else {
                    GradeRange(
                        minGrade = Grade(0.0),
                        maxGrade = Grade(10.0)
                    )
                }

                TaskEventItem(
                    id = "task_$daysAgo ($int)",
                    title = FeedTextMock.getRandomTitle(),
                    description = if (int == 1) {
                        UiText.DynamicString(FeedTextMock.getRandomDescription())
                    } else {
                        UiText.StringResourceId(Res.string.no_description)
                    },
                    author = mockUser,
                    lastUpdateDateTime = eventDateTime,
                    attachments = Attachment.FileType.entries
                        .mapIndexed { index, type ->
                            Attachment.FileAttachment(
                                url = "https://example.com/attachment_$daysAgo#$index",
                                fileName = type.toString(),
                                fileSize = Random.nextLong(100000, 500000),
                                fileType = type,
                                id = index.toString()
                            )
                        }.plus(
                            Attachment.FileType.entries
                                .mapIndexed { index, type ->
                                    Attachment.FileAttachment(
                                        url = "https://example.com/attachment_$daysAgo#$index",
                                        fileName = type.toString(),
                                        fileSize = Random.nextLong(100000, 500000),
                                        fileType = type,
                                        id = index.toString()
                                    )
                                }
                        ),
                    receivers = listOf(
                        "ИУ9-62Б",
                        "ИУ9-61Б"
                    ),
                    grade = grade,
                    deadLine = deadlineDateTime,
                    status = taskStatus
                )
            }

            localTasks.addAll(tasks)
        }

        updateTasksBlocks()
    }

    private fun updateTasksBlocks() {
        localTasksBlocks.clear()

        // Разделяем задачи на выполненные и текущие
        val completedTasks = localTasks.filter { it.status is TaskStatus.Completed }
        val currentTasks = localTasks.filter { it.status !is TaskStatus.Completed }

        var blockId = 0

        // Обрабатываем текущие задачи - группируем по статусам
        // Порядок: REJECTED, ISSUED, UNDER_CHECK, NOT_ISSUED

        // 1. Отклоненные задачи
        val rejectedTasks = currentTasks.filter { it.status is TaskStatus.Rejected }
        if (rejectedTasks.isNotEmpty()) {
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.rejected_tasks).asList(),
                    tasks = rejectedTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.REJECTED_TASKS
                )
            )
        }

        val issuedTasks = currentTasks.filter { it.status is TaskStatus.Issued }
        if (issuedTasks.isNotEmpty()) {
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.given_tasks).asList(),
                    tasks = issuedTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.GIVEN_TASKS
                )
            )
        }

        val underCheckTasks = currentTasks.filter { it.status is TaskStatus.UnderCheck }
        if (underCheckTasks.isNotEmpty()) {
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.under_check).asList(),
                    tasks = underCheckTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.UNDER_CHECK
                )
            )
        }

        val notIssuedTasks = currentTasks.filter { it.status is TaskStatus.NotIssued }
        if (notIssuedTasks.isNotEmpty()) {
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.not_given_tasks).asList(),
                    tasks = notIssuedTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.NOT_ISSUED_TASKS
                )
            )
        }

        // Обрабатываем выполненные задачи - группируем по датам
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val yesterdayDate = currentDate.minus(1, DateTimeUnit.DAY)

        // Группируем выполненные задачи по датам
        val groupedCompletedTasks = completedTasks.groupBy { task ->
            val taskDate = task.lastUpdateDateTime.date

            when {
                taskDate == currentDate -> "today"
                taskDate == yesterdayDate -> "yesterday"
                else -> "${taskDate.month}_${taskDate.year}"
            }
        }

        // Добавляем блок "Сегодня" для выполненных задач
        groupedCompletedTasks["today"]?.let { todayTasks ->
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.today).asList(),
                    tasks = todayTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.COMPLETED_TASKS
                )
            )
        }

        // Добавляем блок "Вчера" для выполненных задач
        groupedCompletedTasks["yesterday"]?.let { yesterdayTasks ->
            localTasksBlocks.add(
                TasksEventsBlock(
                    id = blockId++,
                    title = UiText.StringResourceId(Res.string.yesterday).asList(),
                    tasks = yesterdayTasks.sortedByDescending { it.lastUpdateDateTime },
                    blockType = TasksEventsBlock.BlockType.COMPLETED_TASKS
                )
            )
        }

        // Добавляем блоки по месяцам для выполненных задач
        groupedCompletedTasks.entries
            .filter { it.key != "today" && it.key != "yesterday" }
            .groupBy {
                val (month, year) = it.key.split("_")
                "$month $year"
            }
            .forEach { (monthYear, entries) ->
                // Собираем все задания за месяц
                val monthTasks = entries.flatMap { it.value }

                // Получаем месяц и год из ключа
                val (monthStr, yearStr) = monthYear.split(" ")
                val month = Month.valueOf(monthStr.uppercase())
                val year = yearStr.toInt()

                // Создаем заголовок для месяца
                val monthResource = getMonthResourceName(month)!!
                val titleText = listOf(
                    UiText.StringResourceId(monthResource),
                    UiText.DynamicString(year.toString())
                )

                localTasksBlocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = titleText,
                        tasks = monthTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = TasksEventsBlock.BlockType.COMPLETED_TASKS
                    )
                )
            }
    }

    override fun fetchTasksEvents(filter: Set<BlockType>): Flow<List<TasksEventsBlock>> {
        return flow {
            emit(localTasksBlocks.filter { it.blockType in filter })
        }
    }

    override fun fetchReceivers(): Flow<List<String>> {
        return flow {
            emit(listOf(
                "ИУ9-62Б", "ИУ9-61Б", "ТралалелоТралала", "Баллерино Капучино"
            ))
        }
    }

    override fun getTask(eventId: String?): Flow<TaskEventItem?> {
        return flow {
            emit(localTasks.firstOrNull { it.id == eventId })
        }
    }

    override suspend fun actualizeTasks(): EmptyResult<DataError.Remote> {
        localTasks.clear()
        localTasksBlocks.clear()
        init()
        return Result.Success(Unit)
    }

    override suspend fun actualizeTask(eventId: String?): EmptyResult<DataError.Remote> {
        return Result.Success(Unit)
    }

    override suspend fun createTask(event: TaskEventItem): EmptyResult<DataError> {
        // Преобразуем FeedEventItem в TasksEventItem
        localTasks.add(event)

        updateTasksBlocks()
        return Result.Success(Unit)
    }
}
