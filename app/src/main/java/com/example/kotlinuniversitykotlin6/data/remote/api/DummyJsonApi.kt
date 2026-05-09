package com.example.kotlinuniversitykotlin6.data.remote.api

import com.example.kotlinuniversitykotlin6.data.remote.dto.LoginRequestDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.LoginResponseDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.UserDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.UsersResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DummyJsonApi(private val client: HttpClient) {
    private val base = "https://dummyjson.com"

    suspend fun login(username: String, password: String): LoginResponseDto =
        client.post("$base/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(username, password))
        }.body()

    suspend fun getUsers(token: String): List<UserDto> =
        client.get("$base/users") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body<UsersResponseDto>().users

    suspend fun getUserById(id: Int, token: String): UserDto =
        client.get("$base/users/$id") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
}