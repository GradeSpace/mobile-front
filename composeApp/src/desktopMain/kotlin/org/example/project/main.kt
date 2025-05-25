package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import org.example.project.app.App
import org.example.project.di.initKoin

fun main() = application {
    initKoin()
    FileKit.init(appId = "GradeSpace")
    Window(
        onCloseRequest = ::exitApplication,
        title = "mobile-front",
    ) {
        App()
    }
}