package org.example.project.features.feed.di

import org.example.project.features.feed.data.repository.FeedMockRepository
import org.example.project.features.feed.domain.FeedRepository
import org.example.project.features.feed.presentation.feed_list.FeedListViewModel
import org.example.project.features.feed.presentation.feed_notification.FeedNotificationViewModel
import org.example.project.features.feed.presentation.notification_create.NotificationCreateViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val feedModule = module {
    singleOf(::FeedMockRepository).bind<FeedRepository>()
    viewModelOf(::FeedListViewModel)
    viewModelOf(::FeedNotificationViewModel)
    viewModelOf(::NotificationCreateViewModel)
}