package com.example.kotlinuniversitykotlin6.domain.model

data class NobelPrize(
    val year: String,
    val category: String,
    val laureates: List<Laureate>
)