package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.mapper.toDomain
import com.example.kotlinuniversitykotlin6.data.remote.api.NobelApi
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize

class NobelRepositoryImpl(private val api: NobelApi) : NobelRepository {
    override suspend fun getPrizes(year: Int?, category: String?): List<NobelPrize> =
        api.getNobelPrizes(year = year, category = category)
            .nobelPrizes.map { it.toDomain() }
}