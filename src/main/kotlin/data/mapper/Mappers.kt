package org.example.data.mapper

import org.example.data.dto.LaureateResponseDto
import org.example.data.dto.NobelPrizeResponseDto
import org.example.data.dto.NobelPrizeSummaryResponse
import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize

fun NobelPrize.toResponse() = NobelPrizeResponseDto(
    year = year,
    category = category,
    overallMotivation = overallMotivation,
    laureates = laureates.map { it.toResponse() }
)

fun NobelPrize.toSummary() = NobelPrizeSummaryResponse(
    year = year,
    category = category,
    laureatesCount = laureates.size,
    overallMotivation = overallMotivation
)

fun Laureate.toResponse() = LaureateResponseDto(
    id = id,
    firstName = firstName,
    lastName = lastName,
    motivation = motivation,
    share = share
)