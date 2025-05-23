package org.example.project.app.navigation.utils

import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.NavigationError
import org.example.project.core.domain.Result
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

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

@OptIn(ExperimentalForeignApi::class)
actual fun NavigationManager.openFile(
    fileUrl: String,
    mimeType: String?
): EmptyResult<NavigationError> {
    return try {
        val nsUrl = NSURL.fileURLWithPath(fileUrl)

        if (!NSFileManager.defaultManager.fileExistsAtPath(fileUrl)) {
            return Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
        }

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)

        val isImage = mimeType?.startsWith("image/") ?: fileUrl.lowercase().let {
            it.endsWith(".jpg") || it.endsWith(".jpeg") || it.endsWith(".png") ||
                it.endsWith(".gif") || it.endsWith(".heic") || it.endsWith(".heif")
        }

        if(isImage) {
            val imageDocumentController =
                UIDocumentInteractionController.interactionControllerWithURL(nsUrl)
            imageDocumentController.delegate =
                object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {
                    override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
                        return rootViewController
                    }
                }

            val previewPresented = imageDocumentController.presentPreviewAnimated(true)

            if (previewPresented) {
                return Result.Success(Unit)
            }

            val openInPresented = imageDocumentController.presentOpenInMenuFromRect(
                rootViewController.view.frame,
                rootViewController.view,
                true
            )

            if (openInPresented) {
                return Result.Success(Unit)
            }
        }

        val documentController = UIDocumentInteractionController.interactionControllerWithURL(nsUrl)

        documentController.delegate =
            object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {
                override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
                    return rootViewController
                }
            }

        val presented = documentController.presentPreviewAnimated(true)

        if (presented) {
            Result.Success(Unit)
        } else {
            Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
        }
    } catch (_: Exception) {
        Result.Error(NavigationError.CANNOT_OPEN_THE_FILE)
    }
}
