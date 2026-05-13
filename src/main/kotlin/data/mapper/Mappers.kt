package org.example.data.mapper

import org.example.data.dto.LaureateResponseDto
import org.example.data.dto.NobelPrizeResponseDto
import org.example.data.dto.NobelPrizeSummaryResponse
import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize

fun NobelPrize.toResponse() = NobelPrizeResponseDto(
    id = id,
    awardYear = awardYear,
    category = category,
    fullName = fullName,
    motivation = motivation,
    laureates = laureates?.map { it.toResponse() } ?: emptyList()
)

fun NobelPrize.toSummary() = NobelPrizeSummaryResponse(
    id = id,
    awardYear = awardYear,
    category = category,
    fullName = fullName,
    motivation = motivation,
    laureatesCount = laureates?.size ?: 0
)

fun Laureate.toResponse() = LaureateResponseDto(
    id = id,
    prizeId = prizeId,
    fullName = fullName,
    portion = portion,
    motivation = motivation,
    portraitUrl = portraitUrl
)