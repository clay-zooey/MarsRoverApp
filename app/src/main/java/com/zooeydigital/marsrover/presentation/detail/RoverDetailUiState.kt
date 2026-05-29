package com.zooeydigital.marsrover.presentation.detail

import com.zooeydigital.marsrover.domain.model.MarsPhoto

sealed interface RoverDetailUiState {
    object Loading : RoverDetailUiState
    object Empty : RoverDetailUiState
    data class Error(val message: String) : RoverDetailUiState
    data class Success(val photos: List<MarsPhoto>) : RoverDetailUiState
}
