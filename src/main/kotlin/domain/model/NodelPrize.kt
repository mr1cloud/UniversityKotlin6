package org.example.domain.model

data class NobelPrize(
    val year: Int,
    val category: String,
    val overallMotivation: String?,
    val laureates: List<Laureate>
)