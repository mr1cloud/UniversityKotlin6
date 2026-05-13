package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.local.TokenDataStore
import com.example.kotlinuniversitykotlin6.data.remote.api.NobelApi
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val api: NobelApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {
    override suspend fun login(username: String, password: String) {
        val response = api.login(username, password)
        tokenDataStore.saveToken(response.token)
    }

    override suspend fun logout() = tokenDataStore.clearToken()

    override suspend fun getToken(): String? = tokenDataStore.getToken()

    override fun getTokenFlow(): Flow<String?> = tokenDataStore.tokenFlow
}