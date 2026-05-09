package com.example.kotlinuniversitykotlin6.data.remote.api

import com.example.kotlinuniversitykotlin6.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page")  page: Int  = 1,
        @Query("limit") limit: Int = 30
    ): List<PhotoDto>
}