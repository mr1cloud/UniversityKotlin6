package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.model.User
import com.example.kotlinuniversitykotlin6.domain.repository.UserRepository

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): List<User> = repository.getUsers()
}

class GetUserByIdUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): User = repository.getUserById(id)
}