package com.zooeydigital.marsrover.data.marsvista

import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.model.RoverCamera

internal object MarsVistaRoverMapper {
    fun map(response: MarsVistaRoversResponse): List<MarsRover> =
        response.data
            .map { rover -> rover.toMarsRover() }

    fun mapSingle(rover: MarsVistaRoverDto): MarsRover =
        rover.toMarsRover()

    private fun MarsVistaRoverDto.toMarsRover(): MarsRover =
        MarsRover(
            id = id,
            name = attributes.name,
            landingDate = attributes.landingDate,
            launchDate = attributes.launchDate,
            totalPhotos = attributes.totalPhotos,
            cameras = relationships?.cameras?.map { camera ->
                RoverCamera(
                    name = camera.attributes?.name ?: camera.id,
                    fullName = camera.attributes?.fullName.orEmpty(),
                )
            }.orEmpty(),
            maxDate = attributes.maxDate,
        )
}
