package org.example.project.features.feed.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.no_description
import mobile_front.composeapp.generated.resources.today
import mobile_front.composeapp.generated.resources.yesterday
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.data.utils.getMonthResourceName
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.presentation.UiText
import org.example.project.features.feed.data.mock.FeedTextMock
import org.example.project.features.feed.domain.FeedAction
import org.example.project.features.feed.domain.FeedEventItem
import org.example.project.features.feed.domain.FeedEventsBlock
import org.example.project.features.feed.domain.FeedEventsBlock.BlockType
import org.example.project.features.feed.domain.FeedRepository
import kotlin.random.Random

class FeedMockRepository : FeedRepository {

    val localEvents = mutableListOf<FeedEventItem>()
    val localEventsBlocks = mutableListOf<FeedEventsBlock>()

    init {
        init()
    }

    private fun init() {
        val mockUser = User(
            uid = "user1",
            name = "Иван",
            surname = "Иванов"
        )

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        for (daysAgo in 0..49) {
            val eventDate = currentDateTime.date.minus(daysAgo, DateTimeUnit.DAY)

            val eventTime = LocalTime(
                hour = Random.nextInt(8, 20),
                minute = Random.nextInt(0, 60)
            )

            val eventDateTime = LocalDateTime(eventDate, eventTime)

            val events = (1..2).map { int ->
                // Создаем список действий, включая PerformedAction для некоторых событий
                val actionsList = mutableListOf<FeedAction>()

                // Добавляем обычные кнопки действий
                for (i in 0 until Random.nextInt(3)) {
                    actionsList.add(
                        FeedAction.ButtonAction(
                            actionName = "Действие #$i ${daysAgo + 1} ($int)",
                            action = { }
                        )
                    )
                }

                if (actionsList.isEmpty()) {
                    if ((daysAgo + int) % 2 == 0) {
                        val options =
                            listOf("Вариант A", "Вариант B", "Вариант C", "Принято", "Отклонено")
                        val selectedOption = options[Random.nextInt(options.size)]

                        actionsList.add(
                            FeedAction.PerformedAction(
                                title = "Выбрано: $selectedOption"
                            )
                        )
                    }
                }


                FeedEventItem(
                    id = "event_$daysAgo ($int)",
                    title = FeedTextMock.getRandomTitle(),
                    description = if (int == 1) {
                        UiText.DynamicString(FeedTextMock.getRandomDescription())
                    } else {
                        UiText.StringResourceId(Res.string.no_description)
                    },
                    author = mockUser,
                    lastUpdateDateTime = eventDateTime,
                    attachments = if (daysAgo % 3 == 0) Attachment.FileType.entries
                        .mapIndexed { index, type ->
                            Attachment.FileAttachment(
                                url = "https://example.com/attachment_$daysAgo#$index",
                                fileName = type.toString(),
                                fileSize = Random.nextLong(100000, 500000),
                                fileType = type
                            )
                        }.plus(
                            Attachment.FileType.entries
                                .mapIndexed { index, type ->
                                    Attachment.FileAttachment(
                                        url = "https://example.com/attachment_$daysAgo#$index",
                                        fileName = type.toString(),
                                        fileSize = Random.nextLong(100000, 500000),
                                        fileType = type
                                    )
                                }
                        ) else emptyList(),
                    actions = actionsList,
                    receivers = listOf(
                        "ИУ9-62Б",
                        "ИУ9-61Б",
                        "ИУ9-61Б",
                        "ИУ9-61Б",
                        "ИУ9-61Б",
                        "ИУ9-61Б"
                    )
                )
            }

            localEvents.addAll(events)
        }

        updateEventsBlocks()
    }

    private fun updateEventsBlocks() {
        localEventsBlocks.clear()

        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val yesterdayDate = currentDate.minus(1, DateTimeUnit.DAY)

        // Группируем события по датам
        val groupedEvents = localEvents.groupBy { event ->
            val eventDate = event.lastUpdateDateTime.date

            when {
                eventDate == currentDate -> "today"
                eventDate == yesterdayDate -> "yesterday"
                else -> "${eventDate.month}_${eventDate.year}"
            }
        }

        var blockId = 0

        // Добавляем блок "Сегодня" если есть события
        groupedEvents["today"]?.let { todayEvents ->
            localEventsBlocks.add(
                FeedEventsBlock(
                    id = blockId++,
                    title = listOf(UiText.StringResourceId(Res.string.today)),
                    events = todayEvents.sortedByDescending { it.lastUpdateDateTime },
                    blockType = BlockType.DEFAULT
                )
            )
        }

        // Добавляем блок "Вчера" если есть события
        groupedEvents["yesterday"]?.let { yesterdayEvents ->
            localEventsBlocks.add(
                FeedEventsBlock(
                    id = blockId++,
                    title = listOf(UiText.StringResourceId(Res.string.yesterday)),
                    events = yesterdayEvents.sortedByDescending { it.lastUpdateDateTime },
                    blockType = BlockType.DEFAULT
                )
            )
        }

        // Добавляем блоки по месяцам
        groupedEvents.entries
            .filter { it.key != "today" && it.key != "yesterday" }
            .groupBy {
                val (month, year) = it.key.split("_")
                "$month $year"
            }
            .forEach { (monthYear, entries) ->
                // Собираем все события за месяц
                val monthEvents = entries.flatMap {
                    it.value.map {
                        it.copy(
                            receivers = listOf("ИУ9-62Б", "ИУ9-61Б")
                        )
                    }
                }

                // Получаем месяц и год из ключа
                val (monthStr, yearStr) = monthYear.split(" ")
                val month = Month.valueOf(monthStr.uppercase())
                val year = yearStr.toInt()

                // Создаем заголовок для месяца (ресурс месяца + текст года)
                val monthResource = getMonthResourceName(month)!!
                val titleList = listOf(
                    UiText.StringResourceId(monthResource),
                    UiText.DynamicString("$year")
                )

                localEventsBlocks.add(
                    FeedEventsBlock(
                        id = blockId++,
                        title = titleList,
                        events = monthEvents.sortedByDescending { it.lastUpdateDateTime },
                        blockType = BlockType.OLD
                    )
                )
            }
    }

    override fun fetchFeedEvents(): Flow<List<FeedEventsBlock>> {
        return flow {
            emit(localEventsBlocks)
        }
    }

    override suspend fun actualizeEvents(): EmptyResult<DataError.Remote> {
        localEvents.clear()
        localEventsBlocks.clear()
        init()
        return Result.Success(Unit)
    }

    override fun getEvent(eventId: String?): Flow<FeedEventItem?> {
        return flow {
            emit(localEvents.firstOrNull {
                it.id == eventId
            })
        }
    }

    override suspend fun actualizeEvent(): EmptyResult<DataError.Remote> {
        return Result.Success(Unit)
    }

    override suspend fun createEvent(event: FeedEventItem): EmptyResult<DataError> {
        localEvents.add(event)
        updateEventsBlocks() // Обновляем блоки после добавления события
        return Result.Success(Unit)
    }
}
