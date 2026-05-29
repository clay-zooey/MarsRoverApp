package com.zooeydigital.marsrover.domain.model

data class MarsRover(
    val id: String,
    val name: String,
    val landingDate: String,
    val launchDate: String,
    val totalPhotos: Int,
    val cameras: List<RoverCamera>,
    val maxDate: String,
)
