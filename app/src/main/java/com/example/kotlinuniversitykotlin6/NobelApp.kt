package com.example.kotlinuniversitykotlin6

import android.app.Application
import com.example.kotlinuniversitykotlin6.data.local.TokenDataStore
import com.example.kotlinuniversitykotlin6.data.remote.KtorClient
import com.example.kotlinuniversitykotlin6.data.remote.api.NobelApi
import com.example.kotlinuniversitykotlin6.domain.repository.AuthRepository
import com.example.kotlinuniversitykotlin6.domain.repository.AuthRepositoryImpl
import com.example.kotlinuniversitykotlin6.domain.repository.NobelRepository
import com.example.kotlinuniversitykotlin6.domain.repository.NobelRepositoryImpl
import com.example.kotlinuniversitykotlin6.domain.usecase.AddFavoriteUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetFavoritesUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetLaureatesUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPrizesUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LoginUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.RemoveFavoriteUseCase
import io.ktor.client.HttpClient
import kotlin.getValue

class NobelApp : Application() {
    val tokenDataStore: TokenDataStore by lazy { TokenDataStore(this) }

    private val httpClient: HttpClient by lazy { KtorClient.httpClient }
    private val api: NobelApi by lazy { NobelApi(httpClient) }

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(api, tokenDataStore) }
    private val nobelRepository: NobelRepository by lazy {
        NobelRepositoryImpl(api, authRepository)
    }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
    val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(authRepository) }
    val getPrizesUseCase: GetPrizesUseCase by lazy { GetPrizesUseCase(nobelRepository) }
    val getLaureatesUseCase: GetLaureatesUseCase by lazy { GetLaureatesUseCase(nobelRepository) }
    val getFavoritesUseCase: GetFavoritesUseCase by lazy { GetFavoritesUseCase(nobelRepository) }
    val addFavoriteUseCase: AddFavoriteUseCase by lazy { AddFavoriteUseCase(nobelRepository) }
    val removeFavoriteUseCase: RemoveFavoriteUseCase by lazy { RemoveFavoriteUseCase(nobelRepository) }
}