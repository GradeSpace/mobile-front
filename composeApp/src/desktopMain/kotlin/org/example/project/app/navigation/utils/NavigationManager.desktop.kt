package org.example.project.app.navigation.utils

import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result
import java.awt.Desktop
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