package com.example.kotlinuniversitykotlin6.domain.repository

import com.example.kotlinuniversitykotlin6.data.mapper.toDomain
import com.example.kotlinuniversitykotlin6.data.remote.api.PicsumApi
import com.example.kotlinuniversitykotlin6.domain.model.Photo

class PhotoRepositoryImpl(
    private val api: PicsumApi
) : PhotoRepository {
    override suspend fun getPhotos(page: Int, limit: Int): List<Photo> =
        api.getPhotos(page, limit).map { it.toDomain() }
}