package org.example.project.features.tasks.domain

import kotlinx.serialization.Serializable

@Serializable
enum class VariantDistributionMode(val index: Int) {
    NONE(0), RANDOM(1), VAR_TO_RECEIVER(2)
}

@Serializable
data class TaskVariant(
    val varNum: Int,
    val receivers: List<String>? = null,
    val text: String? = null,
)