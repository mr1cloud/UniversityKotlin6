package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.local.TokenDataStore
import com.example.kotlinuniversitykotlin6.data.remote.api.DummyJsonApi
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val api: DummyJsonApi,
    private val tokenStore: TokenDataStore
) : AuthRepository {

    override suspend fun login(username: String, password: String): String {
        val token = api.login(username, password).accessToken
        tokenStore.saveToken(token)
        return token
    }

    override suspend fun logout() = tokenStore.clearToken()

    override fun getTokenFlow(): Flow<String?> = tokenStore.tokenFlow

    override suspend fun getToken(): String? = tokenStore.getToken()
}