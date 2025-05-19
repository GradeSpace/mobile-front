package org.example.project.app.navigation.launchers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile

expect suspend fun FileKit.openCameraPicker(): PlatformFile?