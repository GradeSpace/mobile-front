package org.example.project.features.feed.data.repository

import org.example.project.core.presentation.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.before
import mobile_front.composeapp.generated.resources.today
import mobile_front.composeapp.generated.resources.yesterday
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.user.User
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.features.feed.data.mock.FeedTextMock
import org.example.project.features.feed.domain.BlockType
import org.example.project.features.feed.domain.FeedAction
import org.example.project.features.feed.domain.FeedEventItem
import org.example.project.features.feed.domain.FeedEventsBlock
import org.example.project.features.feed.domain.FeedRepository
import kotlin.random.Random

class FeedMockRepository : FeedRepository {

    val localEvents = mutableListOf<FeedEventItem>()
    val localEventsBlocks = mutableListOf<FeedEventsBlock>()

    init {
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
                FeedEventItem(
                    id = "event_$daysAgo ($int)",
                    title = FeedTextMock.getRandomTitle(),
                    description = FeedTextMock.getRandomDescription(),
                    author = mockUser,
                    dateTime = eventDateTime,
                    attachments = if (daysAgo % 3 == 0) listOf(
                        Attachment.ImageAttachment(
                            url = "https://example.com/attachment_$daysAgo"
                        )
                    ) else emptyList(),
                    actions = (0..<Random.nextInt(3)).map {
                        FeedAction(
                            actionName = "Действие #$it ${daysAgo + 1} ($int)",
                            action = { }
                        )
                    }
                )
            }

            localEvents.addAll(events)
        }

        val currentDate =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val yesterdayStartOfDay = LocalDateTime(
            date = currentDate.minus(1, DateTimeUnit.DAY),
            time = LocalTime(0, 0, 0)
        )

        val groupedByDate = localEvents.groupBy { event ->
            val eventDate = event.dateTime.date

            if (eventDate < yesterdayStartOfDay.date) {
                OLD_EVENTS_KEY
            } else {
                eventDate.toString()
            }
        }
        var counter = 0
        groupedByDate.forEach { (dateKey, events) ->
            val isOld = dateKey == OLD_EVENTS_KEY
            val title = if (isOld) {
                UiText.StringResourceId(Res.string.before)
            } else {
                formatDateForTitle(LocalDate.parse(dateKey))
            }

            localEventsBlocks.add(
                FeedEventsBlock(
                    title = title,
                    events = events.sortedByDescending { it.dateTime },
                    id = counter++,
                    blockType = when {
                        isOld -> BlockType.OLD
                        else -> BlockType.DEFAULT
                    }
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
        return Result.Success(Unit)
    }

    override suspend fun getEvent(eventId: String): Flow<FeedEventItem?> {
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
        return Result.Success(Unit)
    }

    private fun formatDateForTitle(date: LocalDate): UiText {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when (date) {
            currentDate -> UiText.StringResourceId(Res.string.today)
            currentDate.minus(
                1,
                DateTimeUnit.DAY
            ) -> UiText.StringResourceId(Res.string.yesterday)

            else -> UiText.DynamicString(date.toString())
        }
    }
}

private const val OLD_EVENTS_KEY = "older_than_month"
