package com.zooeydigital.marsrover.presentation.detail

import com.zooeydigital.marsrover.core.common.AppError
import com.zooeydigital.marsrover.domain.model.MarsPhoto
import com.zooeydigital.marsrover.domain.model.MarsRover

data class RoverDetailScreenState(
    val rover: MarsRover? = null,
    val selectedDate: String = "",
    val photosState: PhotosState = PhotosState.Loading
)

sealed interface PhotosState {
    object Loading : PhotosState
    object Empty : PhotosState
    sealed interface Error : PhotosState {
        object InvalidRover : Error
        data class StandardError(val error: AppError) : Error
    }
    data class Success(val photos: List<MarsPhoto>) : PhotosState
}
