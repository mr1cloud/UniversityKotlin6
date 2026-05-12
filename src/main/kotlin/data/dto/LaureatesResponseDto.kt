package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaureateResponseDto(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val motivation: String,
    val share: Int
)