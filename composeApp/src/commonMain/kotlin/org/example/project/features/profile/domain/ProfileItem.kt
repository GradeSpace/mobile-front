package org.example.project.features.profile.domain

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.dark_mode
import mobile_front.composeapp.generated.resources.english
import mobile_front.composeapp.generated.resources.i24_dark_mode
import mobile_front.composeapp.generated.resources.i24_language
import mobile_front.composeapp.generated.resources.i24_light_mode
import mobile_front.composeapp.generated.resources.i24_logout
import mobile_front.composeapp.generated.resources.i24_profile
import mobile_front.composeapp.generated.resources.i24_routine
import mobile_front.composeapp.generated.resources.language
import mobile_front.composeapp.generated.resources.light_mode
import mobile_front.composeapp.generated.resources.logout
import mobile_front.composeapp.generated.resources.personal_data
import mobile_front.composeapp.generated.resources.russian
import mobile_front.composeapp.generated.resources.system_mode
import mobile_front.composeapp.generated.resources.theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class ProfileItemColor {
    DEFAULT, RED
}

enum class ProfileItem(
    val iconRes: DrawableResource,
    val title: StringResource,
    var subtitle: StringResource? = null,
    val color: ProfileItemColor = ProfileItemColor.DEFAULT
) {
    PERSONAL_DATA(
        iconRes = Res.drawable.i24_profile,
        title = Res.string.personal_data,
        color = ProfileItemColor.DEFAULT
    ),
    RUSSIAN_LANGUAGE(
        iconRes = Res.drawable.i24_language,
        title = Res.string.language,
        subtitle = Res.string.russian,
        color = ProfileItemColor.DEFAULT
    ),
    ENGLISH_LANGUAGE(
        iconRes = Res.drawable.i24_language,
        title = Res.string.language,
        subtitle = Res.string.english,
        color = ProfileItemColor.DEFAULT
    ),
    DARK_THEME(
        iconRes = Res.drawable.i24_dark_mode,
        title = Res.string.theme,
        subtitle = Res.string.dark_mode,
        color = ProfileItemColor.DEFAULT,
    ),
    LIGHT_THEME(
        iconRes = Res.drawable.i24_light_mode,
        title = Res.string.theme,
        subtitle = Res.string.light_mode,
        color = ProfileItemColor.DEFAULT,
    ),
    SYSTEM_THEME(
        iconRes = Res.drawable.i24_routine,
        title = Res.string.theme,
        subtitle = Res.string.system_mode,
        color = ProfileItemColor.DEFAULT,
    ),
    EXIT(
        iconRes = Res.drawable.i24_logout,
        title = Res.string.logout,
        color = ProfileItemColor.RED
    )
}