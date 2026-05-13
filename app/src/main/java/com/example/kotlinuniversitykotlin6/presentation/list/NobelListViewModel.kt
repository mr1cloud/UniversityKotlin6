package com.example.kotlinuniversitykotlin6.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.domain.usecase.AddFavoriteUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPrizesUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NobelListViewModel(
    private val getPrizesUseCase: GetPrizesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NobelListUiState>(NobelListUiState.Loading)
    val uiState: StateFlow<NobelListUiState> = _uiState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

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
                _uiState.value = NobelListUiState.Success(getPrizesUseCase(year, category))
            } catch (e: Exception) {
                _uiState.value = NobelListUiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }

    fun toggleFavorite(prize: NobelPrize) {
        viewModelScope.launch {
            val isFav = prize.id in _favoriteIds.value
            if (isFav) {
                removeFavoriteUseCase(prize.id)
                _favoriteIds.value -= prize.id
            } else {
                addFavoriteUseCase(prize.id)
                _favoriteIds.value += prize.id
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch { logoutUseCase(); onDone() }
    }

    class Factory(
        private val getPrizesUseCase: GetPrizesUseCase,
        private val addFavoriteUseCase: AddFavoriteUseCase,
        private val removeFavoriteUseCase: RemoveFavoriteUseCase,
        private val logoutUseCase: LogoutUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NobelListViewModel(
                getPrizesUseCase, addFavoriteUseCase, removeFavoriteUseCase, logoutUseCase
            ) as T
    }
}

sealed class NobelListUiState {
    object Loading : NobelListUiState()
    data class Success(val prizes: List<NobelPrize>) : NobelListUiState()
    data class Error(val message: String) : NobelListUiState()
}