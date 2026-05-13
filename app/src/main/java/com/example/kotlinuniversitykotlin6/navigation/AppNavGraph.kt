package com.example.kotlinuniversitykotlin6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinuniversitykotlin6.NobelApp
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.presentation.detail.LaureateDetailScreen
import com.example.kotlinuniversitykotlin6.presentation.list.NobelListScreen
import com.example.kotlinuniversitykotlin6.presentation.login.LoginScreen
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavGraph(app: NobelApp, startRoute: String) {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(navController, startDestination = startRoute) {
        composable("login") {
            LoginScreen(
                loginUseCase = app.loginUseCase,
                onLoginSuccess = {
                    navController.navigate("prizes") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("prizes") {
            NobelListScreen(
                getPrizesUseCase = app.getPrizesUseCase,
                addFavoriteUseCase = app.addFavoriteUseCase,
                removeFavoriteUseCase = app.removeFavoriteUseCase,
                logoutUseCase = app.logoutUseCase,
                onLaureateClick = { prize, laureate ->
                    val prizeJson = URLEncoder.encode(gson.toJson(prize), "UTF-8")
                    val laureateJson = URLEncoder.encode(gson.toJson(laureate), "UTF-8")
                    navController.navigate("laureate_detail/$prizeJson/$laureateJson")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("prizes") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "laureate_detail/{prize}/{laureate}",
            arguments = listOf(
                navArgument("prize") { type = NavType.StringType },
                navArgument("laureate") { type = NavType.StringType }
            )
        ) { back ->
            val prize = gson.fromJson(
                URLDecoder.decode(back.arguments?.getString("prize"), "UTF-8"),
                NobelPrize::class.java
            )
            val laureate = gson.fromJson(
                URLDecoder.decode(back.arguments?.getString("laureate"), "UTF-8"),
                Laureate::class.java
            )
            LaureateDetailScreen(
                prize = prize,
                laureate = laureate,
                onBack = { navController.popBackStack() }
            )
        }
    }
}