package org.example.di

import io.ktor.server.application.Application
import org.example.controller.AuthController
import org.example.controller.PrizesController
import org.example.data.repository.NobelRepositoryImpl
import org.example.data.repository.UserRepositoryImpl
import org.example.domain.repository.NobelRepository
import org.example.domain.repository.UserRepository
import org.example.domain.usecase.GetLaureatesUseCase
import org.example.domain.usecase.GetPrizeUseCase
import org.example.domain.usecase.GetPrizesUseCase
import org.example.domain.usecase.LoginUseCase

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val nobelRepository: NobelRepository by lazy { NobelRepositoryImpl() }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository) }
    val getLaureatesUseCase: GetLaureatesUseCase by lazy { GetLaureatesUseCase(nobelRepository) }
    val getPrizesUseCase: GetPrizesUseCase by lazy { GetPrizesUseCase(nobelRepository) }
    val getPrizeUseCase: GetPrizeUseCase by lazy { GetPrizeUseCase(nobelRepository) }

    val authController: AuthController by lazy { AuthController(loginUseCase) }
    val prizesController: PrizesController by lazy { PrizesController(
        getPrizesUseCase,
        getPrizeUseCase,
        getLaureatesUseCase
    ) }
}

fun Application.appModule() {
    println("DI инициализирован")
}