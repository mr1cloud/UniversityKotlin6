package com.example.kotlinuniversitykotlin6.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): String
    suspend fun logout()
    fun getTokenFlow(): Flow<String?>
    suspend fun getToken(): String?
}