package org.example.project.core.presentation

import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.error_disk_full
import mobile_front.composeapp.generated.resources.error_email_exists
import mobile_front.composeapp.generated.resources.error_invalid_password
import mobile_front.composeapp.generated.resources.error_no_internet
import mobile_front.composeapp.generated.resources.error_not_found
import mobile_front.composeapp.generated.resources.error_request_timeout
import mobile_front.composeapp.generated.resources.error_serialization
import mobile_front.composeapp.generated.resources.error_too_many_requests
import mobile_front.composeapp.generated.resources.error_unknown
import mobile_front.composeapp.generated.resources.error_user_not_found
import org.example.project.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.NOT_FOUND -> Res.string.error_not_found
        DataError.Auth.USER_NOT_FOUND -> Res.string.error_user_not_found
        DataError.Auth.INVALID_PASSWORD -> Res.string.error_invalid_password
        DataError.Auth.EMAIL_EXISTS -> Res.string.error_email_exists
    }
    
    return UiText.StringResourceId(stringRes)
}