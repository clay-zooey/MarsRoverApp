package com.zooeydigital.marsrover.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zooeydigital.marsrover.domain.model.AppError
import com.zooeydigital.marsrover.domain.model.toAppError
import com.zooeydigital.marsrover.domain.usecase.GetMarsPhotosUseCase
import com.zooeydigital.marsrover.domain.usecase.GetMarsRoversUseCase
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
    private val getMarsRovers: GetMarsRoversUseCase,
    private val getMarsPhotos: GetMarsPhotosUseCase,
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
            getMarsRovers()
                .catch { throwable ->
                    _screenState.update {
                        it.copy(photosState = PhotosState.Error.StandardError(throwable.toAppError()))
                    }
                }
                .collect { rovers ->
                    val rover = rovers.firstOrNull { it.id == roverId }
                    if (rover != null) {
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                            .format(java.util.Date())

                        _screenState.update {
                            it.copy(
                                rover = rover,
                                selectedDate = today,
                                photosState = PhotosState.Loading
                            )
                        }
                        loadPhotos(today)
                    } else {
                        _screenState.update {
                            it.copy(photosState = PhotosState.Error.StandardError(AppError.Unknown("Rover not found.")))
                        }
                    }
                }
        }
    }

    fun onDateSelected(date: String) {
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
            _screenState.update {
                it.copy(
                    selectedDate = date,
                    photosState = PhotosState.Loading,
                    currentPage = 1,
                    isLastPageReached = false,
                    isPaginationLoading = false
                )
            }
            getMarsPhotos(roverId = roverId, date = date, page = 1)
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
                    _screenState.update {
                        it.copy(
                            photosState = nextPhotosState,
                            isLastPageReached = photos.size < PAGE_SIZE
                        )
                    }
                }
        }
    }

    fun loadNextPage() {
        val currentState = _screenState.value
        if (roverId.isEmpty() ||
            currentState.selectedDate.isEmpty() ||
            currentState.isPaginationLoading ||
            currentState.isLastPageReached ||
            currentState.photosState !is PhotosState.Success) {
            return
        }

        viewModelScope.launch {
            _screenState.update { it.copy(isPaginationLoading = true) }
            val nextPage = currentState.currentPage + 1
            getMarsPhotos(roverId = roverId, date = currentState.selectedDate, page = nextPage)
                .catch { throwable ->
                    _screenState.update { it.copy(isPaginationLoading = false) }
                }
                .collect { newPhotos ->
                    _screenState.update { state ->
                        val currentPhotosList = (state.photosState as? PhotosState.Success)?.photos.orEmpty()
                        val updatedPhotos = currentPhotosList + newPhotos
                        state.copy(
                            photosState = PhotosState.Success(updatedPhotos),
                            currentPage = nextPage,
                            isPaginationLoading = false,
                            isLastPageReached = newPhotos.size < PAGE_SIZE
                        )
                    }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}
