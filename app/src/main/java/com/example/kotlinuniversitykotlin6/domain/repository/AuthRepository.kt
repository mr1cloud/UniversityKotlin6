package com.example.kotlinuniversitykotlin6.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String)
    suspend fun logout()
    suspend fun getToken(): String?
    fun getTokenFlow(): Flow<String?>
}