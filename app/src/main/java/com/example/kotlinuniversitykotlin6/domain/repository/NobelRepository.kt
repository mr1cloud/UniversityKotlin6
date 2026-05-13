package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize

interface NobelRepository {
    suspend fun getPrizes(year: Int? = null, category: String? = null): List<NobelPrize>
    suspend fun getLaureates(year: Int, category: String): List<Laureate>
    suspend fun getFavorites(): List<NobelPrize>
    suspend fun addFavorite(prizeId: Int): Boolean
    suspend fun removeFavorite(prizeId: Int): Boolean
}