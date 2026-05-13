package com.example.kotlinuniversitykotlin6.domain.model

data class NobelPrize(
    val id: Int,
    val awardYear: Int,
    val category: String,
    val fullName: String,
    val motivation: String?,
    val laureates: List<Laureate>
)