package org.example.project.app.navigation.utils

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result
import java.io.File

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

actual fun NavigationManager.openFile(fileUrl: String, mimeType: String?): EmptyResult<NavigationError> {
    return try {
        val context = navController.context
        val file = File(fileUrl)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            if (mimeType != null) {
                setDataAndType(uri, mimeType)
            } else {
                data = uri
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent, null)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
    }
}