package org.example.domain.usecase

import org.example.domain.repository.FavoritesRepository

class AddFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(userId: Int, prizeId: Int): Boolean =
        repository.addFavorite(userId, prizeId)
}