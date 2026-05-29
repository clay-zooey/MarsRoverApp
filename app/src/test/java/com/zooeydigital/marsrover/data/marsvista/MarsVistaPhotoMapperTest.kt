package com.zooeydigital.marsrover.data.marsvista

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarsVistaPhotoMapperTest {

    @Test
    fun map_whenAllImageSizesAvailable_prioritizesMedium() {
        val images = MarsVistaPhotoImagesDto(
            small = "http://small.jpg",
            medium = "http://medium.jpg",
            large = "http://large.jpg",
            full = "http://full.jpg"
        )
        val response = createMockResponse(images)

        val result = MarsVistaPhotoMapper.map(response)

        assertEquals(1, result.size)
        assertEquals("http://medium.jpg", result[0].imageUrl)
        assertEquals("http://full.jpg", result[0].fullResUrl)
    }

    @Test
    fun map_whenMediumMissing_prioritizesLarge() {
        val images = MarsVistaPhotoImagesDto(
            small = "http://small.jpg",
            medium = null,
            large = "http://large.jpg",
            full = "http://full.jpg"
        )
        val response = createMockResponse(images)

        val result = MarsVistaPhotoMapper.map(response)

        assertEquals(1, result.size)
        assertEquals("http://large.jpg", result[0].imageUrl)
    }

    @Test
    fun map_whenMediumAndLargeMissing_prioritizesFull() {
        val images = MarsVistaPhotoImagesDto(
            small = "http://small.jpg",
            medium = null,
            large = null,
            full = "http://full.jpg"
        )
        val response = createMockResponse(images)

        val result = MarsVistaPhotoMapper.map(response)

        assertEquals(1, result.size)
        assertEquals("http://full.jpg", result[0].imageUrl)
    }

    @Test
    fun map_whenOnlySmallAvailable_prioritizesSmall() {
        val images = MarsVistaPhotoImagesDto(
            small = "http://small.jpg",
            medium = null,
            large = null,
            full = null
        )
        val response = createMockResponse(images)

        val result = MarsVistaPhotoMapper.map(response)

        assertEquals(1, result.size)
        assertEquals("http://small.jpg", result[0].imageUrl)
    }

    @Test
    fun map_whenNoImagesAvailable_returnsEmptyList() {
        val response = createMockResponse(null)

        val result = MarsVistaPhotoMapper.map(response)

        assertTrue(result.isEmpty())
    }

    @Test
    fun map_whenImagesObjectPresentButAllUrlsNull_returnsEmptyList() {
        val images = MarsVistaPhotoImagesDto(
            small = null,
            medium = null,
            large = null,
            full = null
        )
        val response = createMockResponse(images)

        val result = MarsVistaPhotoMapper.map(response)

        assertTrue(result.isEmpty())
    }

    private fun createMockResponse(images: MarsVistaPhotoImagesDto?): MarsVistaPhotosResponse {
        val photoDto = MarsVistaPhotoDto(
            id = 101L,
            attributes = MarsVistaPhotoAttributesDto(images = images)
        )
        return MarsVistaPhotosResponse(data = listOf(photoDto))
    }
}
