package com.zooeydigital.marsrover.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
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
class RoverDetailViewModel @Inject constructor(
    private val repository: MarsRoverRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _roverState = MutableStateFlow<MarsRover?>(null)
    val roverState: StateFlow<MarsRover?> = _roverState.asStateFlow()

    private val _uiState = MutableStateFlow<RoverDetailUiState>(RoverDetailUiState.Loading)
    val uiState: StateFlow<RoverDetailUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val roverId: String = checkNotNull(savedStateHandle["roverId"]) {
        "roverId must be passed as an argument to RoverDetailScreen"
    }

    init {
        loadRoverDetails()
    }

    private fun loadRoverDetails() {
        viewModelScope.launch {
            _uiState.value = RoverDetailUiState.Loading
            repository.getRovers()
                .catch { throwable ->
                    _uiState.value = RoverDetailUiState.Error(throwable.toUiMessage())
                }
                .collect { rovers ->
                    val rover = rovers.firstOrNull { it.id == roverId }
                    if (rover != null) {
                        _roverState.value = rover
                        _selectedDate.value = rover.maxDate
                        loadPhotos(rover.maxDate)
                    } else {
                        _uiState.value = RoverDetailUiState.Error("Rover not found.")
                    }
                }
        }
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        loadPhotos(date)
    }

    fun onRetryClick() {
        if (_roverState.value == null) {
            loadRoverDetails()
        } else {
            loadPhotos(_selectedDate.value)
        }
    }

    private fun loadPhotos(date: String) {
        if (roverId.isEmpty() || date.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = RoverDetailUiState.Loading
            repository.getPhotos(roverId, date)
                .catch { throwable ->
                    _uiState.value = RoverDetailUiState.Error(throwable.toUiMessage())
                }
                .collect { photos ->
                    _uiState.value = if (photos.isEmpty()) {
                        RoverDetailUiState.Empty
                    } else {
                        RoverDetailUiState.Success(photos)
                    }
                }
        }
    }

    private fun Throwable.toUiMessage(): String =
        when (this) {
            is HttpException -> "Mars Vista is unavailable right now. Please try again."
            is IOException -> "Check your internet connection and try again."
            else -> "Unable to load Mars photos. Please try again."
        }
}
