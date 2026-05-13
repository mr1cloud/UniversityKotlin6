package org.example.domain.usecase

import org.example.domain.repository.FavoritesRepository

class RemoveFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(userId: Int, prizeId: Int): Boolean =
        repository.removeFavorite(userId, prizeId)
}