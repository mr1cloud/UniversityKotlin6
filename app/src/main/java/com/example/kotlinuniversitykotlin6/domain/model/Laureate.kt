package com.example.kotlinuniversitykotlin6.domain.model

data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String,
    val country: String,
    val portraitUrl: String?
)