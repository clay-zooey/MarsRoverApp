package com.zooeydigital.marsrover.domain.usecase

import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMarsRoversUseCase @Inject constructor(
    private val repository: MarsRoverRepository,
) {
    operator fun invoke(): Flow<List<MarsRover>> = repository.getRovers()
}
