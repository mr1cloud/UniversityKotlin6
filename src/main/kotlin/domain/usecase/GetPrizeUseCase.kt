package org.example.domain.usecase

import org.example.domain.model.NobelPrize
import org.example.domain.repository.NobelRepository

class GetPrizeUseCase(private val repository: NobelRepository) {
    operator fun invoke(year: Int, category: String): NobelPrize? =
        repository.getPrize(year, category)
}