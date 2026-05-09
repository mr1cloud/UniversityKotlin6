package com.example.kotlinuniversitykotlin6.data.mapper

import com.example.kotlinuniversitykotlin6.data.remote.dto.LaureateDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.NobelPrizeDto
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize

fun NobelPrizeDto.toDomain() = NobelPrize(
    year = awardYear,
    category = category?.en ?: "N/A",
    laureates = laureates?.map { it.toDomain() } ?: emptyList()
)

fun LaureateDto.toDomain() = Laureate(
    id = id,
    fullName = fullName?.en ?: knownName?.en ?: "Организация",
    motivation = motivation?.en ?: "Мотивация не указана",
    country = birth?.place?.country?.en ?: "Неизвестно",
    portraitUrl = links?.firstOrNull { it.rel == "portrait" }?.href
)