package org.example.project.features.tasks.domain

import org.example.project.core.presentation.UiText

data class TasksEventsBlock(
    val id: Int,
    val tasks: List<TaskEventItem>,
    val title: List<UiText>,
    val blockType: BlockType
) {
    enum class BlockType {
        REJECTED_TASKS,
        GIVEN_TASKS,
        UNDER_CHECK,
        NOT_ISSUED_TASKS,
        COMPLETED_TASKS;

        override fun toString(): String {
            return when(this) {
                COMPLETED_TASKS -> "Выполненные"
                REJECTED_TASKS -> "Отклоненные"
                GIVEN_TASKS -> "Выданные"
                NOT_ISSUED_TASKS -> "Не выданные"
                UNDER_CHECK -> "На проверке"
            }
        }
    }
}
