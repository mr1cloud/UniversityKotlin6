package org.example.routing

import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorredoc.redoc
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.example.di.AppContainer

fun Application.configureRouting() {
    routing {
        route("api.json") {
            openApi()
        }
        route("docs") {
            redoc("/api.json") {
                pageTitle = "My API Docs"
            }
        }
        get("/test") {
            call.respond(mapOf("message" to "Это открытый маршрут!"))
        }
    }

    AppContainer.authController.configure(this)
    AppContainer.prizesController.configure(this)
    AppContainer.favoritesController.configure(this)
}