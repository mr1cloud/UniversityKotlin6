package org.example.controller

import io.github.smiley4.ktoropenapi.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.authenticate
import org.example.data.dto.LaureateResponseDto
import org.example.data.dto.NobelPrizeResponseDto
import org.example.data.mapper.toResponse
import org.example.domain.usecase.GetLaureatesUseCase
import org.example.domain.usecase.GetPrizesUseCase

class PrizesController(
    private val getPrizesUseCase: GetPrizesUseCase,
    private val getLaureatesUseCase: GetLaureatesUseCase
) {
    fun configure(application: Application) {
        application.routing {
            authenticate("auth-jwt") {
                get("/prizes", {
                    tags = listOf("Prizes")
                    description = "Get a specific Nobel Prize by year and category"
                    request {
                        queryParameter<Int>("year") {
                            description = "The year of the Nobel Prize"
                        }
                        queryParameter<String>("category") {
                            description = "The category of the Nobel Prize (e.g. physics, chemistry)"
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Nobel Prize details"
                            body<NobelPrizeResponseDto> {
                                description = "Full details of the requested prize"
                            }
                        }
                        HttpStatusCode.NotFound to {
                            description = "Prize not found"
                        }
                        HttpStatusCode.BadRequest to {
                            description = "Invalid year or category parameter"
                        }
                    }
                }) {
                    val year = call.parameters["year"]?.toIntOrNull()
                    val category = call.parameters["category"]

                    val prize = getPrizesUseCase(year, category)
                    if (prize == null) {
                        call.respond(
                            HttpStatusCode.NotFound,
                            mapOf("error" to "Премия за $year год в категории '$category' не найдена")
                        )
                        return@get
                    }

                    call.respond(HttpStatusCode.OK, prize.toResponse())
                }

                get("/prizes/{year}/{category}/laureates", {
                    tags = listOf("Prizes")
                    description = "Get laureates of a specific Nobel Prize"
                    request {
                        pathParameter<Int>("year") {
                            description = "The year of the Nobel Prize"
                        }
                        pathParameter<String>("category") {
                            description = "The category of the Nobel Prize"
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "List of laureates"
                            body<List<LaureateResponseDto>> {
                                description = "Laureates of the requested prize"
                            }
                        }
                        HttpStatusCode.NotFound to {
                            description = "No laureates found for the given year and category"
                        }
                        HttpStatusCode.BadRequest to {
                            description = "Invalid year or category parameter"
                        }
                    }
                }) {
                    val year = call.parameters["year"]?.toIntOrNull()
                    val category = call.parameters["category"]

                    if (year == null || category == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("error" to "Укажите год (число) и категорию")
                        )
                        return@get
                    }

                    val laureates = getLaureatesUseCase(year, category)
                    if (laureates.isEmpty()) {
                        call.respond(
                            HttpStatusCode.NotFound,
                            mapOf("error" to "Лауреаты не найдены")
                        )
                        return@get
                    }

                    call.respond(HttpStatusCode.OK, laureates.map { it.toResponse() })
                }
            }
        }
    }
}