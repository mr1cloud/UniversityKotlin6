package com.example.kotlinuniversitykotlin6.data.mapper

import com.example.kotlinuniversitykotlin6.data.remote.dto.PhotoDto
import com.example.kotlinuniversitykotlin6.domain.model.Photo

fun PhotoDto.toDomain(): Photo = Photo(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    downloadUrl = downloadUrl,
    thumbnailUrl = "https://picsum.photos/id/$id/400/300"
)