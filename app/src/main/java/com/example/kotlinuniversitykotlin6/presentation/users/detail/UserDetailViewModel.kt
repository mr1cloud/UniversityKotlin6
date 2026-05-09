package com.example.kotlinuniversitykotlin6.presentation.users.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinuniversitykotlin6.domain.model.User
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUserByIdUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class Success(val user: User) : UserDetailUiState()
    data class Error(val message: String) : UserDetailUiState()
}

class UserDetailViewModel(
    private val userId: Int,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading
            try {
                _uiState.value = UserDetailUiState.Success(getUserByIdUseCase(userId))
            } catch (e: Exception) {
                _uiState.value = UserDetailUiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch { logoutUseCase(); onDone() }
    }

    class Factory(
        private val userId: Int,
        private val getUserByIdUseCase: GetUserByIdUseCase,
        private val logoutUseCase: LogoutUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UserDetailViewModel(userId, getUserByIdUseCase, logoutUseCase) as T
    }
}