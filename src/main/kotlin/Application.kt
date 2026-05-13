package org.example

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.AuthScheme
import io.github.smiley4.ktoropenapi.config.AuthType
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.example.data.database.DatabaseFactory
import org.example.data.database.LaureateTable
import org.example.data.database.PrizeTable
import org.example.data.database.UserTable
import org.example.di.appModule
import org.example.plugins.configureAuthentication
import org.example.plugins.configureCallLogging
import org.example.plugins.configureContentNegotiation
import org.example.plugins.configureStatusPages
import org.example.routing.configureRouting
import org.example.security.PasswordHasher
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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

    DatabaseFactory.init()
    transaction {
        if (UserTable.selectAll().where { UserTable.username eq "admin" }.empty()) {
            UserTable.insert {
                it[username] = "admin"
                it[passwordHash] = PasswordHasher.hash("secret123")
                it[role] = "admin"
            }
        }
    }

    transaction {
        if (PrizeTable.selectAll().empty()) {
            val physicsId = PrizeTable.insertAndGetId {
                it[awardYear] = 2023
                it[category] = "physics"
                it[fullName] = "The Nobel Prize in Physics 2023"
                it[motivation] = "for experimental methods that generate attosecond pulses of light"
                it[detailLink] = "https://www.nobelprize.org/prizes/physics/2023/summary/"
            }.value

            LaureateTable.insert {
                it[prizeId] = physicsId
                it[fullName] = "Pierre Agostini"
                it[portion] = "1/3"
                it[motivation] =
                    "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }
            LaureateTable.insert {
                it[prizeId] = physicsId
                it[fullName] = "Ferenc Krausz"
                it[portion] = "1/3"
                it[motivation] =
                    "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }
            LaureateTable.insert {
                it[prizeId] = physicsId
                it[fullName] = "Anne L'Huillier"
                it[portion] = "1/3"
                it[motivation] =
                    "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }

            val medicineId = PrizeTable.insertAndGetId {
                it[awardYear] = 2023
                it[category] = "medicine"
                it[fullName] = "The Nobel Prize in Physiology or Medicine 2023"
                it[motivation] = "for discoveries concerning nucleoside base modifications that enabled mRNA vaccines"
                it[detailLink] = "https://www.nobelprize.org/prizes/medicine/2023/summary/"
            }.value

            LaureateTable.insert {
                it[prizeId] = medicineId
                it[fullName] = "Katalin Karikó"
                it[portion] = "1/2"
                it[motivation] = "for their discoveries concerning nucleoside base modifications"
            }
            LaureateTable.insert {
                it[prizeId] = medicineId
                it[fullName] = "Drew Weissman"
                it[portion] = "1/2"
                it[motivation] = "for their discoveries concerning nucleoside base modifications"
            }

            val peaceId = PrizeTable.insertAndGetId {
                it[awardYear] = 2023
                it[category] = "peace"
                it[fullName] = "The Nobel Peace Prize 2023"
                it[motivation] = "for her fight against the oppression of women in Iran"
                it[detailLink] = "https://www.nobelprize.org/prizes/peace/2023/summary/"
            }.value

            LaureateTable.insert {
                it[prizeId] = peaceId
                it[fullName] = "Narges Mohammadi"
                it[portion] = "1/1"
                it[motivation] =
                    "for her fight against the oppression of women in Iran and her efforts to promote human rights and freedom for all"
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