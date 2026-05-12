package org.example

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.AuthScheme
import io.github.smiley4.ktoropenapi.config.AuthType
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.example.di.appModule
import org.example.plugins.configureAuthentication
import org.example.plugins.configureCallLogging
import org.example.plugins.configureContentNegotiation
import org.example.plugins.configureStatusPages
import org.example.routing.configureRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(OpenApi) {
        info {
            title = "My API"
            version = "1.0.0"
        }
        server {
            url = "http://localhost:8080"
            description = "Локальный сервер"
        }
        security {
            securityScheme("MyJwtAuth") {
                type = AuthType.HTTP
                scheme = AuthScheme.BEARER
                bearerFormat = "JWT"
            }
        }
    }

    appModule()
    configureContentNegotiation()
    configureCallLogging()
    configureStatusPages()
    configureAuthentication()
    configureRouting()
}