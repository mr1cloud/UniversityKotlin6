package org.example.di

import io.ktor.server.application.Application
import org.example.controller.AuthController
import org.example.controller.FavoritesController
import org.example.controller.PrizesController
import org.example.data.repository.FavoritesRepositoryImpl
import org.example.data.repository.NobelRepositoryImpl
import org.example.data.repository.UserRepositoryImpl
import org.example.domain.repository.FavoritesRepository
import org.example.domain.repository.NobelRepository
import org.example.domain.repository.UserRepository
import org.example.domain.usecase.AddFavoriteUseCase
import org.example.domain.usecase.GetFavoritesUseCase
import org.example.domain.usecase.GetLaureatesUseCase
import org.example.domain.usecase.GetPrizesUseCase
import org.example.domain.usecase.LoginUseCase
import org.example.domain.usecase.RemoveFavoriteUseCase

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val nobelRepository: NobelRepository by lazy { NobelRepositoryImpl() }
    val favoritesRepository: FavoritesRepository by lazy { FavoritesRepositoryImpl() }

    // Login AuthUseCases
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository) }
    // Prizes UseCases
    val getLaureatesUseCase: GetLaureatesUseCase by lazy { GetLaureatesUseCase(nobelRepository) }
    val getPrizesUseCase: GetPrizesUseCase by lazy { GetPrizesUseCase(nobelRepository) }
    // Favorites UseCases
    val addFavoriteUseCase: AddFavoriteUseCase by lazy { AddFavoriteUseCase(favoritesRepository) }
    val getFavoritesUseCase: GetFavoritesUseCase by lazy { GetFavoritesUseCase(favoritesRepository) }
    val removeFavoriteUseCase: RemoveFavoriteUseCase by lazy { RemoveFavoriteUseCase(favoritesRepository) }

    val authController: AuthController by lazy { AuthController(loginUseCase) }
    val prizesController: PrizesController by lazy { PrizesController(
        getPrizesUseCase,
        getLaureatesUseCase
    ) }
    val favoritesController: FavoritesController by lazy { FavoritesController(
        getFavoritesUseCase,
        addFavoriteUseCase,
        removeFavoriteUseCase
    ) }
}

fun Application.appModule() {
    println("DI инициализирован")
}