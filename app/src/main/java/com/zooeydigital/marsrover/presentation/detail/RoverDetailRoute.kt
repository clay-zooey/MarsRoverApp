package com.zooeydigital.marsrover.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RoverDetailRoute(
    viewModel: RoverDetailViewModel = hiltViewModel(),
) {
    val roverState by viewModel.roverState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    RoverDetailScreen(
        rover = roverState,
        uiState = uiState,
        selectedDate = selectedDate,
        onDateSelected = viewModel::onDateSelected,
        onRetryClick = viewModel::onRetryClick,
    )
}
