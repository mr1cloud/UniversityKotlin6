package com.example.kotlinuniversitykotlin6.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val accessToken: String = "",
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val image: String = ""
)

@Serializable
data class UsersResponseDto(
    val users: List<UserDto> = emptyList()
)

@Serializable
data class UserDto(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val image: String = "",
    val phone: String = "",
    val address: AddressDto? = null,
    val company: CompanyDto? = null
)

@Serializable
data class AddressDto(
    val city: String = "",
    val country: String = ""
)

@Serializable
data class CompanyDto(
    val name: String = ""
)