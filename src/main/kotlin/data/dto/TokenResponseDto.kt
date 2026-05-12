package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto (
    val token: String,
    val expiresIn: Int = 1800
)