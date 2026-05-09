package com.example.kotlinuniversitykotlin6.data.mapper

import com.example.kotlinuniversitykotlin6.data.remote.dto.UserDto
import com.example.kotlinuniversitykotlin6.domain.model.User

fun UserDto.toDomain() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    username = username,
    email = email,
    imageUrl = image,
    phone = phone,
    city = address?.city ?: "",
    country = address?.country ?: "",
    company = company?.name ?: ""
)