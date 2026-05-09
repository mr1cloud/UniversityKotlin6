package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.mapper.toDomain
import com.example.kotlinuniversitykotlin6.data.remote.api.DummyJsonApi
import com.example.kotlinuniversitykotlin6.domain.model.User

class UserRepositoryImpl(
    private val api: DummyJsonApi,
    private val authRepository: AuthRepository
) : UserRepository {

    private suspend fun token() =
        authRepository.getToken() ?: error("Не авторизован")

    override suspend fun getUsers(): List<User> =
        api.getUsers(token()).map { it.toDomain() }

    override suspend fun getUserById(id: Int): User =
        api.getUserById(id, token()).toDomain()
}