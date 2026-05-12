package org.example.controller

import io.github.smiley4.ktoropenapi.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.data.dto.LoginRequestDto
import org.example.data.dto.TokenResponseDto
import org.example.domain.usecase.LoginUseCase

class AuthController(private val loginUseCase: LoginUseCase) {
    fun configure(application: Application) {
        application.routing {
            post("/login", {
                tags = listOf("Authentication")
                description = "Endpoint for user login"
                request {
                    body<LoginRequestDto> {
                        description = "Login request payload"
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Successful login"
                        body<TokenResponseDto> {
                            description = "JWT token returned upon successful authentication"
                        }
                    }
                }
            }) {
                val request = call.receive<LoginRequestDto>()
                val token = loginUseCase.invoke(request.username, request.password)

                if (token != null) {
                    call.respond(HttpStatusCode.OK, TokenResponseDto(token = token))
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        mapOf("error" to "Invalid username or password"))
                }
            }
        }
    }
}