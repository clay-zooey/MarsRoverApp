package com.zooeydigital.marsrover.presentation.rovers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zooeydigital.marsrover.domain.model.toAppError
import com.zooeydigital.marsrover.domain.usecase.GetMarsRoversUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

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
                    _uiState.value = RoversUiState.Error(throwable.toAppError())
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
}
