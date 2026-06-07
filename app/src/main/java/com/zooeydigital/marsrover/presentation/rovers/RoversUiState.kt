package com.zooeydigital.marsrover.presentation.rovers

import com.zooeydigital.marsrover.domain.model.AppError
import com.zooeydigital.marsrover.domain.model.MarsRover

sealed interface RoversUiState {
    data object Loading : RoversUiState
    data object Empty : RoversUiState
    data class Success(val rovers: List<MarsRover>) : RoversUiState
    data class Error(val error: AppError) : RoversUiState
}
