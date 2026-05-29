package com.zooeydigital.marsrover.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zooeydigital.marsrover.core.common.AppError
import com.zooeydigital.marsrover.core.common.toAppError
import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RoverDetailViewModel @Inject constructor(
    private val repository: MarsRoverRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _screenState = MutableStateFlow(RoverDetailScreenState())
    val screenState: StateFlow<RoverDetailScreenState> = _screenState.asStateFlow()

    private val roverId: String = savedStateHandle.get<String>("roverId").orEmpty()

    init {
        if (roverId.isEmpty()) {
            _screenState.update {
                it.copy(photosState = PhotosState.Error.InvalidRover)
            }
        } else {
            loadRoverDetails()
        }
    }

    private fun loadRoverDetails() {
        viewModelScope.launch {
            _screenState.update { it.copy(photosState = PhotosState.Loading) }
            repository.getRovers()
                .catch { throwable ->
                    _screenState.update {
                        it.copy(photosState = PhotosState.Error.StandardError(throwable.toAppError()))
                    }
                }
                .collect { rovers ->
                    val rover = rovers.firstOrNull { it.id == roverId }
                    if (rover != null) {
                        _screenState.update {
                            it.copy(
                                rover = rover,
                                selectedDate = rover.maxDate,
                                photosState = PhotosState.Loading
                            )
                        }
                        loadPhotos(rover.maxDate)
                    } else {
                        _screenState.update {
                            it.copy(photosState = PhotosState.Error.StandardError(AppError.Unknown("Rover not found.")))
                        }
                    }
                }
        }
    }

    fun onDateSelected(date: String) {
        _screenState.update { it.copy(selectedDate = date, photosState = PhotosState.Loading) }
        loadPhotos(date)
    }

    fun onRetryClick() {
        val currentState = _screenState.value
        if (currentState.rover == null) {
            loadRoverDetails()
        } else {
            loadPhotos(currentState.selectedDate)
        }
    }

    private fun loadPhotos(date: String) {
        if (roverId.isEmpty() || date.isEmpty()) return
        viewModelScope.launch {
            _screenState.update { it.copy(photosState = PhotosState.Loading) }
            repository.getPhotos(roverId, date)
                .catch { throwable ->
                    _screenState.update {
                        it.copy(photosState = PhotosState.Error.StandardError(throwable.toAppError()))
                    }
                }
                .collect { photos ->
                    val nextPhotosState = if (photos.isEmpty()) {
                        PhotosState.Empty
                    } else {
                        PhotosState.Success(photos)
                    }
                    _screenState.update { it.copy(photosState = nextPhotosState) }
                }
        }
    }
}
