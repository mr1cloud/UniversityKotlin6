package org.example.domain.repository

import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize

interface NobelRepository {
    fun getAllPrizes(): List<NobelPrize>
    fun getPrize(year: Int, category: String): NobelPrize?
    fun getLaureates(year: Int, category: String): List<Laureate>
}