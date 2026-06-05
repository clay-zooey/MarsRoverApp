package com.zooeydigital.marsrover.domain.usecase

import com.zooeydigital.marsrover.domain.model.MarsPhoto
import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMarsPhotosUseCase @Inject constructor(
    private val repository: MarsRoverRepository,
) {
    operator fun invoke(roverId: String, date: String, page: Int = 1): Flow<List<MarsPhoto>> {
        return repository.getPhotos(roverId, date, page)
    }
}
