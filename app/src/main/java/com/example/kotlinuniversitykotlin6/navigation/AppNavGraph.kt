package com.example.kotlinuniversitykotlin6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinuniversitykotlin6.data.local.TokenDataStore
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUserByIdUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUsersUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LoginUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import com.example.kotlinuniversitykotlin6.presentation.login.LoginScreen
import com.example.kotlinuniversitykotlin6.presentation.users.UsersListScreen
import com.example.kotlinuniversitykotlin6.presentation.users.detail.UserDetailScreen
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavGraph(
    tokenDataStore: TokenDataStore,
    loginUseCase: LoginUseCase,
    logoutUseCase: LogoutUseCase,
    getUsersUseCase: GetUsersUseCase,
    getUserByIdUseCase: GetUserByIdUseCase
) {
    val navController = rememberNavController()
    val startRoute = remember {
        val token = runBlocking { tokenDataStore.tokenFlow.firstOrNull() }
        if (token != null) "users" else "login"
    }

    NavHost(navController, startDestination = startRoute) {

        composable("login") {
            LoginScreen(
                loginUseCase = loginUseCase,
                onLoginSuccess = {
                    navController.navigate("users") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("users") {
            UsersListScreen(
                getUsersUseCase = getUsersUseCase,
                logoutUseCase = logoutUseCase,
                onUserClick = { user -> navController.navigate("user_detail/${user.id}") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("users") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "user_detail/{userId}",
            listOf(navArgument("userId") { type = NavType.IntType })
        ) { back ->
            val userId = back.arguments!!.getInt("userId")
            UserDetailScreen(
                userId = userId,
                getUserByIdUseCase = getUserByIdUseCase,
                logoutUseCase = logoutUseCase,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}