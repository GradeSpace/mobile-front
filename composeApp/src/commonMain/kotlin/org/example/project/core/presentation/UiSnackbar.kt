package org.example.project.core.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import org.example.project.core.data.model.Action

data class UiSnackbar(
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val message: String,
    override val withDismissAction: Boolean = true,
    val onActionPerformed: Action = {},
    val onDismiss: Action = {}
): SnackbarVisuals
