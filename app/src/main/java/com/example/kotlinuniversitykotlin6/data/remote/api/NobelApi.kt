package com.example.kotlinuniversitykotlin6.data.remote.api

import com.example.kotlinuniversitykotlin6.data.remote.dto.LaureateResponseDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.LoginRequestDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.NobelPrizeResponseDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.TokenResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class NobelApi(private val client: HttpClient) {

    private val base = "http://10.0.2.2:8080"

    suspend fun login(username: String, password: String): TokenResponseDto =
        client.post("$base/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(username, password))
        }.body()

    suspend fun getPrizes(
        token: String,
        year: Int? = null,
        category: String? = null
    ): List<NobelPrizeResponseDto> =
        client.get("$base/prizes") {
            header(HttpHeaders.Authorization, "Bearer $token")
            year?.let { parameter("year", it) }
            category?.let { parameter("category", it) }
        }.body()

    suspend fun getLaureates(
        token: String,
        year: Int,
        category: String
    ): List<LaureateResponseDto> =
        client.get("$base/prizes/$year/$category/laureates") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()

    suspend fun getFavorites(token: String): List<NobelPrizeResponseDto> =
        client.get("$base/users/me/prizes") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()

    suspend fun addFavorite(token: String, prizeId: Int) =
        client.post("$base/users/me/prizes/$prizeId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.status

    suspend fun removeFavorite(token: String, prizeId: Int) =
        client.delete("$base/users/me/prizes/$prizeId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.status
}