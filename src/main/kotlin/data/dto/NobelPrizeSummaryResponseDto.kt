package org.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeSummaryResponse(
    val year: Int,
    val category: String,
    val laureatesCount: Int,
    val overallMotivation: String?
)