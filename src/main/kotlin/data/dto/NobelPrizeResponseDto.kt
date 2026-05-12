package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeResponseDto(
    val year: Int,
    val category: String,
    val overallMotivation: String?,
    val laureates: List<LaureateResponseDto>
)