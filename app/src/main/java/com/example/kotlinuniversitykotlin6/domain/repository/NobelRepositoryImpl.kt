package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.mapper.toDomain
import com.example.kotlinuniversitykotlin6.data.remote.api.NobelApi
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import io.ktor.http.HttpStatusCode

class NobelRepositoryImpl(
    private val api: NobelApi,
    private val authRepository: AuthRepository
) : NobelRepository {

    private suspend fun token() =
        authRepository.getToken() ?: error("Не авторизован")

    override suspend fun getPrizes(year: Int?, category: String?): List<NobelPrize> =
        api.getPrizes(token(), year, category).map { it.toDomain() }

    override suspend fun getLaureates(year: Int, category: String): List<Laureate> =
        api.getLaureates(token(), year, category).map { it.toDomain() }

    override suspend fun getFavorites(): List<NobelPrize> =
        api.getFavorites(token()).map { it.toDomain() }

    override suspend fun addFavorite(prizeId: Int): Boolean =
        api.addFavorite(token(), prizeId) == HttpStatusCode.OK

    override suspend fun removeFavorite(prizeId: Int): Boolean =
        api.removeFavorite(token(), prizeId) == HttpStatusCode.OK
}