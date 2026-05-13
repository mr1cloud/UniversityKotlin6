package org.example.domain.model

data class Laureate(
    val id: Int,
    val prizeId: Int,
    val fullName: String,
    val portion: String,
    val motivation: String?,
    val portraitUrl: String?
)