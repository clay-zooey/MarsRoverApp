package com.zooeydigital.marsrover.data.marsvista

import com.google.gson.annotations.SerializedName

data class MarsVistaRoversResponse(
    @SerializedName("data") val data: List<MarsVistaRoverDto> = emptyList(),
)

data class MarsVistaRoverResponse(
    @SerializedName("data") val data: MarsVistaRoverDto,
)

data class MarsVistaRoverDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: MarsVistaRoverAttributesDto,
    @SerializedName("relationships") val relationships: MarsVistaRoverRelationshipsDto? = null,
)

data class MarsVistaRoverAttributesDto(
    @SerializedName("name") val name: String,
    @SerializedName("landing_date") val landingDate: String,
    @SerializedName("launch_date") val launchDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("max_sol") val maxSol: Int,
    @SerializedName("max_date") val maxDate: String,
    @SerializedName("total_photos") val totalPhotos: Int,
)

data class MarsVistaRoverRelationshipsDto(
    @SerializedName("cameras") val cameras: List<MarsVistaRoverCameraDto>? = null,
)

data class MarsVistaRoverCameraDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: MarsVistaRoverCameraAttributesDto? = null,
)

data class MarsVistaRoverCameraAttributesDto(
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
)
