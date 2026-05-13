package com.example.kotlinuniversitykotlin6.data.mapper

import com.example.kotlinuniversitykotlin6.data.remote.dto.LaureateResponseDto
import com.example.kotlinuniversitykotlin6.data.remote.dto.NobelPrizeResponseDto
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize

fun NobelPrizeResponseDto.toDomain() = NobelPrize(
    id = id,
    awardYear = awardYear,
    category = category,
    fullName = fullName,
    motivation = motivation,
    laureates = laureates.map { it.toDomain() }
)

fun LaureateResponseDto.toDomain() = Laureate(
    id = id,
    prizeId = prizeId,
    fullName = fullName,
    portion = portion,
    motivation = motivation,
    portraitUrl = portraitUrl
)