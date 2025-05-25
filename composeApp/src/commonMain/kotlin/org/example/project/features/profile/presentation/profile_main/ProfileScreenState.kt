package org.example.project.features.profile.presentation.profile_main

import org.example.project.core.data.model.Action
import org.example.project.core.data.model.user.User
import org.example.project.core.presentation.UiText
import org.example.project.features.profile.domain.ProfileItem
import org.example.project.features.profile.presentation.components.BottomSheetContent

data class ProfileScreenState(
    val error: UiText? = null,
    val isRefreshing: Boolean = true,
    val userInfo: User = User("", ""),
    val profileItems: List<UiProfileItem> = emptyList(),
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetContent: BottomSheetContent? = null
)

data class UiProfileItem(
    val item: ProfileItem,
    val action: Action
)