package org.example.project

import org.example.project.core.data.model.Language

sealed interface Platform {
    data object IOS : Platform
    data object Android : Platform
    data object Desktop : Platform
}

expect fun getPlatform(): Platform

expect fun getSystemLanguage(): Language