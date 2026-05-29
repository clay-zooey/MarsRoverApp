package com.zooeydigital.marsrover.data.marsvista

import com.zooeydigital.marsrover.domain.model.MarsPhoto

internal object MarsVistaPhotoMapper {

    fun map(response: MarsVistaPhotosResponse): List<MarsPhoto> =
        response.data.mapNotNull { photoDto ->
            // Prioritize medium size for performance and load speed, small as a last resort.
            val imageUrl = photoDto.attributes.images?.let { images ->
                images.medium ?: images.large ?: images.full ?: images.small
            }
            if (imageUrl != null) {
                MarsPhoto(
                    id = photoDto.id.toString(),
                    imageUrl = imageUrl,
                    fullResUrl = photoDto.attributes.images.full
                )
            } else {
                null
            }
        }
}
