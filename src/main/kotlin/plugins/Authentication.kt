package org.example.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.example.security.JwtConfig

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.ISSUER
            verifier { JwtConfig.verifier }
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                val userId = credential.payload.getClaim("userId").asInt()
                val exp = credential.payload.expiresAt?.time ?: 0
                val now = System.currentTimeMillis()
                val expired = exp < now

                println("JWT validate called:")
                println("  username: $username")
                println("  userId: $userId")
                println("  exp: $exp")
                println("  now: $now")
                println("  expired: $expired")

                if (username != null && !expired) {
                    println("JWT валидный")
                    JWTPrincipal(credential.payload)
                } else {
                    println("JWT невалидный")
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
}