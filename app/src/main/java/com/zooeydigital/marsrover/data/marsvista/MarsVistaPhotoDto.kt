package com.zooeydigital.marsrover.data.marsvista

import com.google.gson.annotations.SerializedName

data class MarsVistaPhotosResponse(
    @SerializedName("data") val data: List<MarsVistaPhotoDto> = emptyList(),
)

data class MarsVistaPhotoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("attributes") val attributes: MarsVistaPhotoAttributesDto,
)

data class MarsVistaPhotoAttributesDto(
    @SerializedName("images") val images: MarsVistaPhotoImagesDto? = null,
)

data class MarsVistaPhotoImagesDto(
    @SerializedName("small") val small: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("large") val large: String? = null,
    @SerializedName("full") val full: String? = null,
)
