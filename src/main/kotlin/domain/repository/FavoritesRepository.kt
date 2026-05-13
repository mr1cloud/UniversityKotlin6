package org.example.domain.repository

import org.example.domain.model.NobelPrize

interface FavoritesRepository {
    suspend fun getFavorites(userId: Int): List<NobelPrize>
    suspend fun addFavorite(userId: Int, prizeId: Int): Boolean
    suspend fun removeFavorite(userId: Int, prizeId: Int): Boolean
    suspend fun isFavorite(userId: Int, prizeId: Int): Boolean
}