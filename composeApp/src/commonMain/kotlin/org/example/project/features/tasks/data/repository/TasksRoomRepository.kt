package org.example.project.features.tasks.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.given_tasks
import mobile_front.composeapp.generated.resources.not_given_tasks
import mobile_front.composeapp.generated.resources.rejected_tasks
import mobile_front.composeapp.generated.resources.today
import mobile_front.composeapp.generated.resources.under_check
import mobile_front.composeapp.generated.resources.yesterday
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.data.model.user.User
import org.example.project.core.data.utils.getMonthResourceName
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.repository.UserRepository
import org.example.project.core.presentation.UiText
import org.example.project.core.presentation.asList
import org.example.project.features.tasks.data.clearTaskDraft
import org.example.project.features.tasks.data.database.dao.TasksDao
import org.example.project.features.tasks.data.getTaskCreateDraft
import org.example.project.features.tasks.data.mappers.toDomain
import org.example.project.features.tasks.data.mappers.toEntity
import org.example.project.features.tasks.data.mappers.toTaskEntity
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

class TasksRoomRepository(
    private val tasksDao: TasksDao,
    private val dataStorePreferences: DataStorePreferences,
    private val userRepository: UserRepository
) : TasksRepository {

    override fun fetchTasksEvents(filter: Set<BlockType>): Flow<List<TasksEventsBlock>> {
        return tasksDao.getAllTasksWithRelations().map { tasksWithRelations ->
            // Преобразуем сущности в доменные модели
            val domainTasks = tasksWithRelations.map { taskWithRelations ->
                taskWithRelations.task.toDomain(
                    attachments = taskWithRelations.attachments,
                    variants = taskWithRelations.variants
                )
            }

            // Формируем блоки на основе доменных моделей
            createTasksBlocks(domainTasks, filter)
        }
    }

    private fun createTasksBlocks(tasks: List<TaskEventItem>, filter: Set<BlockType>): List<TasksEventsBlock> {
        val blocks = mutableListOf<TasksEventsBlock>()
        var blockId = 0

        // Разделяем задачи на выполненные и текущие
        val completedTasks = tasks.filter { it.status is TaskStatus.Completed }
        val currentTasks = tasks.filter { it.status !is TaskStatus.Completed }

        // Обрабатываем текущие задачи - группируем по статусам
        if (BlockType.REJECTED_TASKS in filter) {
            val rejectedTasks = currentTasks.filter { it.status is TaskStatus.Rejected }
            if (rejectedTasks.isNotEmpty()) {
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.rejected_tasks).asList(),
                        tasks = rejectedTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.REJECTED_TASKS
                    )
                )
            }
        }

        if (BlockType.GIVEN_TASKS in filter) {
            val issuedTasks = currentTasks.filter { it.status is TaskStatus.Issued }
            if (issuedTasks.isNotEmpty()) {
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.given_tasks).asList(),
                        tasks = issuedTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.GIVEN_TASKS
                    )
                )
            }
        }

        if (BlockType.UNDER_CHECK in filter) {
            val underCheckTasks = currentTasks.filter { it.status is TaskStatus.UnderCheck }
            if (underCheckTasks.isNotEmpty()) {
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.under_check).asList(),
                        tasks = underCheckTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.UNDER_CHECK
                    )
                )
            }
        }

        if (BlockType.NOT_ISSUED_TASKS in filter) {
            val notIssuedTasks = currentTasks.filter { it.status is TaskStatus.NotIssued }
            if (notIssuedTasks.isNotEmpty()) {
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.not_given_tasks).asList(),
                        tasks = notIssuedTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.NOT_ISSUED_TASKS
                    )
                )
            }
        }

        // Обрабатываем выполненные задачи только если они включены в фильтр
        if (BlockType.COMPLETED_TASKS in filter) {
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
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.today).asList(),
                        tasks = todayTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.COMPLETED_TASKS
                    )
                )
            }

            // Добавляем блок "Вчера" для выполненных задач
            groupedCompletedTasks["yesterday"]?.let { yesterdayTasks ->
                blocks.add(
                    TasksEventsBlock(
                        id = blockId++,
                        title = UiText.StringResourceId(Res.string.yesterday).asList(),
                        tasks = yesterdayTasks.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.COMPLETED_TASKS
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
                    val month = kotlinx.datetime.Month.valueOf(monthStr.uppercase())
                    val year = yearStr.toInt()

                    // Создаем заголовок для месяца
                    val monthResource = getMonthResourceName(month)!!
                    val titleText = listOf(
                        UiText.StringResourceId(monthResource),
                        UiText.DynamicString(year.toString())
                    )

                    blocks.add(
                        TasksEventsBlock(
                            id = blockId++,
                            title = titleText,
                            tasks = monthTasks.sortedByDescending { it.lastUpdateDateTime },
                            blockType = BlockType.COMPLETED_TASKS
                        )
                    )
                }
        }

        return blocks
    }

    override fun fetchReceivers(): Flow<List<String>> {
        return flow {
            emit(listOf("Group 1", "Group2", "Group 3", "Group 4", "Group 5"))
        }
    }

    override fun getTask(eventId: String?): Flow<TaskEventItem?> {
        if (eventId == null) return kotlinx.coroutines.flow.flowOf(null)

        return tasksDao.getTaskWithRelations(eventId).map { taskWithRelations ->
            taskWithRelations?.let {
                it.task.toDomain(
                    attachments = it.attachments,
                    variants = it.variants
                )
            }
        }
    }

    override suspend fun actualizeTasks(): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API
        return Result.Success(Unit)
    }

    override suspend fun actualizeTask(eventId: String?): EmptyResult<DataError.Remote> {
        // В реальной реализации здесь был бы запрос к API для конкретного задания
        return Result.Success(Unit)
    }

    override suspend fun createTask(event: TaskEventItem): EmptyResult<DataError> {
        try {
            val taskEntity = event.toEntity()

            val attachmentEntities = event.attachments.mapNotNull {
                it.toTaskEntity(event.id)
            }

            val variantEntities = event.status?.let { status ->
                when (status) {
                    is TaskStatus.NotIssued -> {
                        // Если задание еще не выдано, то можем иметь варианты
                        // Здесь нужно получить варианты из черновика
                        val draft = getCreateTaskDraft()
                        draft.variants?.map { it.toEntity(event.id) } ?: emptyList()
                    }
                    else -> emptyList()
                }
            } ?: emptyList()

            tasksDao.insertTaskWithRelations(
                task = taskEntity,
                attachments = attachmentEntities,
                variants = variantEntities
            )

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
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
}
