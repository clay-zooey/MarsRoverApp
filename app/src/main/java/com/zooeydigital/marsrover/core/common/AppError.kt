package com.zooeydigital.marsrover.core.common

import com.zooeydigital.marsrover.core.network.MissingMarsVistaApiKeyException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

sealed interface AppError {
    object MissingApiKey : AppError
    object NoInternet : AppError
    object ServerUnavailable : AppError
    data class Unknown(val message: String) : AppError
}

fun Throwable.toAppError(): AppError =
    when (this) {
        is MissingMarsVistaApiKeyException -> AppError.MissingApiKey
        is UnknownHostException, is IOException -> AppError.NoInternet
        is HttpException -> AppError.ServerUnavailable
        else -> AppError.Unknown(this.localizedMessage.orEmpty())
    }
