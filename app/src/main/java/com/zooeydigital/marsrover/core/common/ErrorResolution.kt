package com.zooeydigital.marsrover.core.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zooeydigital.marsrover.R

@Composable
fun AppError.resolveMessage(fallbackResId: Int): String {
    return when (this) {
        AppError.MissingApiKey -> stringResource(R.string.error_missing_api_key)
        AppError.NoInternet -> stringResource(R.string.error_no_internet)
        AppError.ServerUnavailable -> stringResource(R.string.error_server_unavailable)
        is AppError.Unknown -> this.message.ifBlank { stringResource(fallbackResId) }
    }
}
