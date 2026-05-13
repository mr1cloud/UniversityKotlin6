package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.repository.NobelRepository

class GetLaureatesUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(year: Int, category: String) =
        repository.getLaureates(year, category)
}