package org.example.domain.model

data class Laureate(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val motivation: String,
    val share: Int
)