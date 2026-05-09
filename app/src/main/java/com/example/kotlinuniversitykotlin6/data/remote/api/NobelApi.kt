package com.example.kotlinuniversitykotlin6.data.remote.api

import com.example.kotlinuniversitykotlin6.data.remote.dto.NobelPrizesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NobelApi(private val client: HttpClient) {
    suspend fun getNobelPrizes(
        limit: Int = 50,
        offset: Int = 0,
        year: Int? = null,
        category: String? = null
    ): NobelPrizesResponse =
        client.get("https://api.nobelprize.org/2.1/nobelPrizes") {
            parameter("limit", limit)
            parameter("offset", offset)
            year?.let { parameter("nobelPrizeYear", it) }
            category?.let { parameter("nobelPrizeCategory", it) }
        }.body()
}