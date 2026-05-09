package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize

interface NobelRepository {
    suspend fun getPrizes(year: Int? = null, category: String? = null): List<NobelPrize>
}