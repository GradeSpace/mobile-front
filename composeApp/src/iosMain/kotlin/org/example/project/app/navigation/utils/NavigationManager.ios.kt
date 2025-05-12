package org.example.project.app.navigation.utils

import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun NavigationManager.openUrl(url: String): EmptyResult<NavigationError> {
    return try {
        val nsUrl = requireNotNull(NSURL.URLWithString(url))
        UIApplication.sharedApplication.openURL(
            nsUrl,
            options = mapOf<Any?, Any?>(),
            completionHandler = null
        )
        Result.Success(Unit)
    } catch (_: Exception) {
        Result.Error<NavigationError>(NavigationError.FAILED_TO_OPEN_URL)
    }
}