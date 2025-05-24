package org.example.project.features.auth.di

import org.example.project.features.auth.data.repository.AuthMockRepository
import org.example.project.features.auth.domain.AuthRepository
import org.example.project.features.auth.presentation.auth_screen.AuthViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthMockRepository).bind<AuthRepository>()
    viewModelOf(::AuthViewModel)
}