package org.example.controller

import io.github.smiley4.ktoropenapi.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.data.dto.NobelPrizeResponseDto
import org.example.data.mapper.toResponse
import org.example.domain.usecase.AddFavoriteUseCase
import org.example.domain.usecase.GetFavoritesUseCase
import org.example.domain.usecase.RemoveFavoriteUseCase
import org.example.utils.userId

class FavoritesController(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) {
    fun configure(application: Application) {
        application.routing {
            authenticate("auth-jwt") {
                // GET /users/me/prizes
                get("/users/me/prizes", {
                    tags = listOf("Favorites")
                    description = "Get current user's favorite prizes"
                    response {
                        HttpStatusCode.OK to {
                            description = "List of favorite prizes"
                            body<List<NobelPrizeResponseDto>> {}
                        }
                    }
                }) {
                    val userId = call.userId()
                    val prizes = getFavoritesUseCase(userId)
                    call.respond(HttpStatusCode.OK, prizes.map { it.toResponse() })
                }

                // POST /users/me/prizes/{prizeId}
                post("/users/me/prizes/{prizeId}", {
                    tags = listOf("Favorites")
                    description = "Add a prize to favorites"
                    request {
                        pathParameter<Int>("prizeId") {
                            description = "ID of the prize to add"
                        }
                    }
                    response {
                        HttpStatusCode.OK to { description = "Prize added" }
                        HttpStatusCode.Conflict to { description = "Already in favorites" }
                        HttpStatusCode.BadRequest to { description = "Invalid prizeId" }
                    }
                }) {
                    val userId = call.userId()
                    val prizeId = call.parameters["prizeId"]?.toIntOrNull()
                        ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("error" to "Неверный prizeId")
                        )

                    val added = addFavoriteUseCase(userId, prizeId)
                    if (added) {
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Добавлено в избранное"))
                    } else {
                        call.respond(HttpStatusCode.Conflict, mapOf("error" to "Уже в избранном"))
                    }
                }

                // DELETE /users/me/prizes/{prizeId}
                delete("/users/me/prizes/{prizeId}", {
                    tags = listOf("Favorites")
                    description = "Remove a prize from favorites"
                    request {
                        pathParameter<Int>("prizeId") {
                            description = "ID of the prize to remove"
                        }
                    }
                    response {
                        HttpStatusCode.OK to { description = "Prize removed" }
                        HttpStatusCode.NotFound to { description = "Prize not in favorites" }
                        HttpStatusCode.BadRequest to { description = "Invalid prizeId" }
                    }
                }) {
                    val userId = call.userId()
                    val prizeId = call.parameters["prizeId"]?.toIntOrNull()
                        ?: return@delete call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("error" to "Неверный prizeId")
                        )

                    val removed = removeFavoriteUseCase(userId, prizeId)
                    if (removed) {
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Удалено из избранного"))
                    } else {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Не найдено в избранном"))
                    }
                }
            }
        }
    }
}