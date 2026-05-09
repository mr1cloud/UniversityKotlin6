package com.example.kotlinuniversitykotlin6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinuniversitykotlin6.data.local.TokenDataStore
import com.example.kotlinuniversitykotlin6.data.remote.KtorClient
import com.example.kotlinuniversitykotlin6.data.remote.api.DummyJsonApi
import com.example.kotlinuniversitykotlin6.domain.repository.AuthRepositoryImpl
import com.example.kotlinuniversitykotlin6.domain.repository.UserRepositoryImpl
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUserByIdUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUsersUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LoginUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import com.example.kotlinuniversitykotlin6.navigation.AppNavGraph
import com.example.kotlinuniversitykotlin6.ui.theme.KotlinUniversityKotlin6Theme

class MainActivity : ComponentActivity() {
    val tokenDataStore: TokenDataStore by lazy { TokenDataStore(this) }
    private val api: DummyJsonApi by lazy { DummyJsonApi(KtorClient.httpClient) }
    private val authRepository by lazy { AuthRepositoryImpl(api, tokenDataStore) }
    private val userRepository by lazy { UserRepositoryImpl(api, authRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
        val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(authRepository) }
        val getUsersUseCase: GetUsersUseCase by lazy { GetUsersUseCase(userRepository) }
        val getUserByIdUseCase: GetUserByIdUseCase by lazy { GetUserByIdUseCase(userRepository) }
        setContent {
            KotlinUniversityKotlin6Theme {
                AppNavGraph(
                    tokenDataStore = tokenDataStore,
                    loginUseCase = loginUseCase,
                    logoutUseCase = logoutUseCase,
                    getUsersUseCase = getUsersUseCase,
                    getUserByIdUseCase = getUserByIdUseCase
                )
            }
        }
    }
}
