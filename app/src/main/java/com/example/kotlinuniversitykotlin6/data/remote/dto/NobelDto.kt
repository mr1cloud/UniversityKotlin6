package com.example.kotlinuniversitykotlin6.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizesResponse(
    val nobelPrizes: List<NobelPrizeDto> = emptyList()
)

@Serializable
data class NobelPrizeDto(
    val awardYear: String = "",
    val category: CategoryDto? = null,
    val laureates: List<LaureateDto>? = null
)

@Serializable
data class CategoryDto(
    val en: String = ""
)

@Serializable
data class MultiLangDto(
    val en: String = "",
    val ru: String? = null
)

@Serializable
data class LaureateDto(
    val id: String = "",
    val fullName: MultiLangDto? = null,
    val knownName: MultiLangDto? = null,
    val motivation: MultiLangDto? = null,
    val birth: BirthDto? = null,
    val links: List<LinkDto>? = null
)

@Serializable
data class BirthDto(
    val place: PlaceDto? = null
)

@Serializable
data class PlaceDto(
    val country: MultiLangDto? = null
)

@Serializable
data class LinkDto(
    val rel: String = "",
    val href: String = ""
)