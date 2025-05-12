package org.example.project.app.navigation.utils

import android.content.Intent
import androidx.core.net.toUri
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result

actual fun NavigationManager.openUrl(url: String): EmptyResult<NavigationError> {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val context = navController.context
        context.startActivity(intent)
        Result.Success(Unit)
    } catch (_: Exception) {
        Result.Error(NavigationError.FAILED_TO_OPEN_URL)
    }
}