package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.domain.repository.NobelRepository

class GetNobelPrizesUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(year: Int? = null, category: String? = null): List<NobelPrize> =
        repository.getPrizes(year, category)
}