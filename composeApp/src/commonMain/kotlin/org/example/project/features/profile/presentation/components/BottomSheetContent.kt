package org.example.project.features.profile.presentation.components

import org.example.project.core.data.model.Action
import org.example.project.core.presentation.UiText

data class BottomSheetContent(
    val title: UiText? = null,
    val items: List<BottomSheetItem> = emptyList()
)

data class BottomSheetItem(
    val text: UiText,
    val isSelected: Boolean,
    val onClick: Action
)