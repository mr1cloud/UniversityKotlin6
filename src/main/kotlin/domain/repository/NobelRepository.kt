package org.example.domain.repository

import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize

interface NobelRepository {
    suspend fun getAllPrizes(year: Int?, category: String?): NobelPrize?
    suspend fun getLaureates(year: Int, category: String): List<Laureate>
}