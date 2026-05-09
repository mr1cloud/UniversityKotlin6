package com.example.kotlinuniversitykotlin6.domain.usecase

import com.example.kotlinuniversitykotlin6.domain.model.Photo
import com.example.kotlinuniversitykotlin6.domain.repository.PhotoRepository

class GetPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 30): List<Photo> =
        repository.getPhotos(page, limit)
}
