package org.example.project.app.navigation.launchers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openCameraPicker

actual suspend fun FileKit.openCameraPicker(): PlatformFile? {
    return FileKit.openCameraPicker()
}