package org.example.project.core.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

class UiEventsManager(
    private val snackbarHostState: SnackbarHostState
) {

    @Composable
    fun subscribeEventsOnLifecycle(
        uiEventsBlock: suspend CoroutineScope.() -> Unit
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiEventsBlock()
            }
        }
    }

    suspend fun showSnackBar(snackbar: UiSnackbar) {
        when (snackbarHostState.showSnackbar(snackbar)) {
            SnackbarResult.Dismissed -> snackbar.onDismiss
            SnackbarResult.ActionPerformed -> snackbar.onActionPerformed
        }
    }
}