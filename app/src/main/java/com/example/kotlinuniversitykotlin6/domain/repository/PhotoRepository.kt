package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(page: Int = 1, limit: Int = 30): List<Photo>
}