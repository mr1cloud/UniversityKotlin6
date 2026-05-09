package com.example.kotlinuniversitykotlin6.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.domain.usecase.GetNobelPrizesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NobelListUiState {
    object Loading : NobelListUiState()
    data class Success(val prizes: List<NobelPrize>) : NobelListUiState()
    data class Error(val message: String) : NobelListUiState()
}

class NobelListViewModel(
    private val getNobelPrizesUseCase: GetNobelPrizesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NobelListUiState>(NobelListUiState.Loading)
    val uiState: StateFlow<NobelListUiState> = _uiState.asStateFlow()

    val categories = listOf("physics", "chemistry", "literature", "peace", "medicine", "economics")
    var selectedYear: Int? = null; private set
    var selectedCategory: String? = null; private set

    init {
        loadPrizes()
    }

    fun loadPrizes(year: Int? = selectedYear, category: String? = selectedCategory) {
        selectedYear = year
        selectedCategory = category
        viewModelScope.launch {
            _uiState.value = NobelListUiState.Loading
            try {
                _uiState.value = NobelListUiState.Success(
                    getNobelPrizesUseCase(year = year, category = category)
                )
            } catch (e: Exception) {
                _uiState.value = NobelListUiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }

    class Factory(private val useCase: GetNobelPrizesUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NobelListViewModel(useCase) as T
    }
}