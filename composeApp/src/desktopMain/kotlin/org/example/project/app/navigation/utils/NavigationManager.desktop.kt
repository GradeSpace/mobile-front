package org.example.project.app.navigation.utils

import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result
import java.awt.Desktop
import java.io.File
import java.net.URI

actual fun NavigationManager.openUrl(url: String): EmptyResult<NavigationError> {
    return try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                .isSupported(Desktop.Action.BROWSE)
        ) {
            Desktop.getDesktop().browse(URI(url))
            Result.Success(Unit)
        } else {
            Result.Error<NavigationError>(NavigationError.URL_NOT_SUPPORTED)
        }
    } catch (_: Exception) {
        Result.Error<NavigationError>(NavigationError.FAILED_TO_OPEN_URL)
    }
}

actual fun NavigationManager.openFile(
    fileUrl: String,
    mimeType: String?
): EmptyResult<NavigationError> {
    return try {
        val filePath = if (fileUrl.startsWith("file://")) {
            fileUrl.substring(7)
        } else {
            fileUrl
        }

        val file = File(filePath)

        if (file.exists() && Desktop.isDesktopSupported() &&
            Desktop.getDesktop().isSupported(Desktop.Action.OPEN)
        ) {
            Desktop.getDesktop().open(file)
            Result.Success(Unit)
        } else {
            Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
        }
    } catch (e: Exception) {
        Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
    }
}