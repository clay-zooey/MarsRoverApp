package com.zooeydigital.marsrover.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RoverDetailRoute(
    viewModel: RoverDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    RoverDetailScreen(
        state = state,
        onDateSelected = viewModel::onDateSelected,
        onRetryClick = viewModel::onRetryClick,
    )
}
