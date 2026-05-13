package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.repository.NobelRepository

class AddFavoriteUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(prizeId: Int) = repository.addFavorite(prizeId)
}