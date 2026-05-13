package org.example.domain.usecase

import org.example.domain.model.NobelPrize
import org.example.domain.repository.FavoritesRepository

class GetFavoritesUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(userId: Int): List<NobelPrize> =
        repository.getFavorites(userId)
}