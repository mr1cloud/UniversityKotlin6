package org.example.domain.usecase

import org.example.domain.model.NobelPrize
import org.example.domain.repository.NobelRepository

class GetPrizesUseCase(private val repository: NobelRepository) {
    operator fun invoke(): List<NobelPrize> = repository.getAllPrizes()
}