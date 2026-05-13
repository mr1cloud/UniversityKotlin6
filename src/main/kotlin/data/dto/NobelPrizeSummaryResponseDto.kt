package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeSummaryResponse(
    val id: Int,
    val awardYear: Int,
    val category: String,
    val fullName: String,
    val motivation: String?,
    val laureatesCount: Int,
)