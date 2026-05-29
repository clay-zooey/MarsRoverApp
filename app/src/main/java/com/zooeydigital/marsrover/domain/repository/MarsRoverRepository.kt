package com.zooeydigital.marsrover.domain.repository

import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.model.MarsPhoto
import kotlinx.coroutines.flow.Flow

interface MarsRoverRepository {
    fun getRovers(): Flow<List<MarsRover>>
    fun getPhotos(roverId: String, date: String): Flow<List<MarsPhoto>>
}
