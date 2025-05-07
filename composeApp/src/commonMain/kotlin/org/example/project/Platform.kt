package org.example.project

sealed interface Platform {
    data object IOS : Platform
    data object Android : Platform
    data object Desktop : Platform
}

expect fun getPlatform(): Platform