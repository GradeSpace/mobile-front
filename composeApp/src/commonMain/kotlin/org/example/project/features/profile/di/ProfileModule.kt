package org.example.project.features.profile.di

import org.example.project.features.profile.data.repository.ProfileRepositoryImpl
import org.example.project.features.profile.domain.ProfileRepository
import org.example.project.features.profile.presentation.profile_main.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileModule = module {
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()
    viewModelOf(::ProfileViewModel)
}