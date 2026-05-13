package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaureateResponseDto(
    val id: Int,
    val prizeId: Int,
    val fullName: String,
    val portion: String,
    val motivation: String?,
    val portraitUrl: String?
)