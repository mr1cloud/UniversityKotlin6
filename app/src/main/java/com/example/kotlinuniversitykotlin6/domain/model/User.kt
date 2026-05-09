package com.example.kotlinuniversitykotlin6.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val imageUrl: String,
    val phone: String,
    val city: String,
    val country: String,
    val company: String
) {
    val fullName: String get() = "$firstName $lastName"
}