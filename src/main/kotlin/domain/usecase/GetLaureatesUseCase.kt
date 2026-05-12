package org.example.domain.usecase

import org.example.domain.model.Laureate
import org.example.domain.repository.NobelRepository

class GetLaureatesUseCase(private val repository: NobelRepository) {
    operator fun invoke(year: Int, category: String): List<Laureate> =
        repository.getLaureates(year, category)
}