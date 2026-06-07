package com.zooeydigital.marsrover.domain.model

import com.zooeydigital.marsrover.data.network.MissingMarsVistaApiKeyException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class ThrowableToAppErrorTest {

    @Test
    fun toAppError_whenMissingMarsVistaApiKeyException_returnsMissingApiKey() {
        val exception = MissingMarsVistaApiKeyException()
        val error = exception.toAppError()
        assertEquals(AppError.MissingApiKey, error)
    }

    @Test
    fun toAppError_whenIOException_returnsNoInternet() {
        val exception = IOException("No connection")
        val error = exception.toAppError()
        assertEquals(AppError.NoInternet, error)
    }

    @Test
    fun toAppError_whenUnknownHostException_returnsNoInternet() {
        val exception = UnknownHostException("Could not resolve host")
        val error = exception.toAppError()
        assertEquals(AppError.NoInternet, error)
    }

    @Test
    fun toAppError_whenHttpException_returnsServerUnavailable() {
        val errorResponse = Response.error<Any>(503, "".toResponseBody(null))
        val exception = HttpException(errorResponse)
        
        val error = exception.toAppError()
        
        assertEquals(AppError.ServerUnavailable, error)
    }

    @Test
    fun toAppError_whenGenericException_returnsUnknown() {
        val exception = NullPointerException("Some random crash")
        val error = exception.toAppError()
        
        assertTrue(error is AppError.Unknown)
        assertEquals("Some random crash", (error as AppError.Unknown).message)
    }
}
