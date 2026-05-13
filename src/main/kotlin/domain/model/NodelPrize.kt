package org.example.domain.model

data class NobelPrize(
    val id: Int,
    val awardYear: Int,
    val category: String,
    val fullName: String,
    val motivation: String?,
    val detailLink: String?,
    val laureates: List<Laureate>?
)