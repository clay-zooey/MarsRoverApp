package com.zooeydigital.marsrover.data.marsvista

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarsVistaApi {
    @GET("api/v2/rovers")
    suspend fun getRovers(): MarsVistaRoversResponse

    @GET("api/v2/rovers/{id}")
    suspend fun getRover(
        @Path("id") roverId: String,
    ): MarsVistaRoverResponse

    @GET("api/v2/photos")
    suspend fun getPhotosForDate(
        @Query("rovers") roverId: String,
        @Query("earth_date") earthDate: String,
        @Query("per_page") perPage: Int = 100,
    ): MarsVistaPhotosResponse
}
