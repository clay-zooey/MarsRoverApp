package com.zooeydigital.marsrover.presentation.rovers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zooeydigital.marsrover.data.repository.MissingMarsVistaApiKeyException
import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.usecase.GetMarsRoversUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class RoversViewModel @Inject constructor(
    private val getMarsRovers: GetMarsRoversUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoversUiState>(RoversUiState.Loading)
    val uiState: StateFlow<RoversUiState> = _uiState.asStateFlow()

    init {
        loadRovers()
    }

    fun onRetryClick() {
        loadRovers()
    }

    private fun loadRovers() {
        viewModelScope.launch {
            _uiState.value = RoversUiState.Loading

            getMarsRovers()
                .catch { throwable ->
                    _uiState.value = RoversUiState.Error(throwable.toUiMessage())
                }
                .collect { rovers ->
                    _uiState.value = if (rovers.isEmpty()) {
                        RoversUiState.Empty
                    } else {
                        RoversUiState.Success(rovers)
                    }
                }
        }
    }

    private fun Throwable.toUiMessage(): String =
        when (this) {
            is MissingMarsVistaApiKeyException ->
                "Add a Mars Vista API key before loading rover data."
            is HttpException ->
                "Mars Vista is unavailable right now. Please try again."
            is IOException ->
                "Check your internet connection and try again."
            else ->
                "Unable to load Mars rovers. Please try again."
        }
}
