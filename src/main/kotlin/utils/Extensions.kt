package org.example.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

fun ApplicationCall.userId(): Int {
    val principal = principal<JWTPrincipal>()!!
    val username = principal.payload.subject
    return principal.payload.getClaim("userId").asInt()
}