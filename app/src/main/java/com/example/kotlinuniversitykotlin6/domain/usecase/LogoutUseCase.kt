package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}