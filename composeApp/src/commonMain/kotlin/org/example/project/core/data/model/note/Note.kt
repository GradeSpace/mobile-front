package org.example.project.core.data.model.note

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Представляет набор оценок для элемента
 */
@Serializable
data class GradeRange(
    val minGrade: Grade? = null,
    val maxGrade: Grade? = null,
    val currentGrade: Grade? = null
)

/**
 * Представляет числовую оценку
 */
@JvmInline
@Serializable
value class Grade(val value: Double) {
    override fun toString(): String {
        val intPart = value.toInt()
        val fracPart = ((value - intPart) * 10).toInt()

        return if (fracPart == 0) {
            "$intPart"
        } else {
            "$intPart.$fracPart"
        }
    }
}
