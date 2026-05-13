package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeResponseDto(
    val id: Int,
    val awardYear: Int,
    val category: String,
    val fullName: String,
    val motivation: String?,
    val laureates: List<LaureateResponseDto>
)