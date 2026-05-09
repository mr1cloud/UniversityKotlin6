package com.example.kotlinuniversitykotlin6.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinuniversitykotlin6.domain.model.Photo
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PhotoListUiState {
    object Loading : PhotoListUiState()
    data class Success(val photos: List<Photo>) : PhotoListUiState()
    data class Error(val message: String) : PhotoListUiState()
}

class PhotoListViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotoListUiState>(PhotoListUiState.Loading)
    val uiState: StateFlow<PhotoListUiState> = _uiState.asStateFlow()

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = PhotoListUiState.Loading
            try {
                _uiState.value = PhotoListUiState.Success(getPhotosUseCase())
            } catch (e: Exception) {
                _uiState.value = PhotoListUiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }

    class Factory(private val getPhotosUseCase: GetPhotosUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PhotoListViewModel(getPhotosUseCase) as T
    }
}