package com.example.kotlinuniversitykotlin6.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinuniversitykotlin6.domain.model.User
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUsersUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UsersUiState {
    object Loading : UsersUiState()
    data class Success(val users: List<User>) : UsersUiState()
    data class Error(val message: String) : UsersUiState()
}

class UsersViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            try {
                _uiState.value = UsersUiState.Success(getUsersUseCase())
            } catch (e: Exception) {
                _uiState.value = UsersUiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch { logoutUseCase(); onDone() }
    }

    class Factory(
        private val getUsersUseCase: GetUsersUseCase,
        private val logoutUseCase: LogoutUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UsersViewModel(getUsersUseCase, logoutUseCase) as T
    }
}