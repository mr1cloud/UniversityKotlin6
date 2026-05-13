package com.example.kotlinuniversitykotlin6.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val username: String,
    val password: String
)

@Serializable
data class TokenResponseDto(
    val token: String,
    val expiresIn: Int
)

@Serializable
data class NobelPrizeResponseDto(
    val id: Int,
    val awardYear: Int,
    val category: String,
    val fullName: String,
    val motivation: String? = null,
    val laureates: List<LaureateResponseDto> = emptyList()
)

@Serializable
data class LaureateResponseDto(
    val id: Int,
    val prizeId: Int,
    val fullName: String,
    val portion: String,
    val motivation: String? = null,
    val portraitUrl: String? = null
)