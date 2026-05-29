package com.zooeydigital.marsrover.presentation.rovers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zooeydigital.marsrover.domain.model.MarsRover

@Composable
fun RoversRoute(
    onRoverClick: (MarsRover) -> Unit,
    viewModel: RoversViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoversScreen(
        uiState = uiState,
        onRoverClick = onRoverClick,
        onRetryClick = viewModel::onRetryClick,
    )
}
