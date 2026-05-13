package org.example.domain.usecase

import org.example.domain.model.NobelPrize
import org.example.domain.repository.NobelRepository

class GetPrizesUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(year: Int?, category: String?): NobelPrize? =
        repository.getAllPrizes(year, category)
}