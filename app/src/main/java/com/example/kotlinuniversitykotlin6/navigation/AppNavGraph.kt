package com.example.kotlinuniversitykotlin6.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.domain.usecase.GetNobelPrizesUseCase
import com.example.kotlinuniversitykotlin6.presentation.detail.LaureateDetailScreen
import com.example.kotlinuniversitykotlin6.presentation.list.NobelListScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavGraph(getNobelPrizesUseCase: GetNobelPrizesUseCase) {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(navController, startDestination = "nobel_list") {
        composable("nobel_list") {
            NobelListScreen(
                getNobelPrizesUseCase = getNobelPrizesUseCase,
                onLaureateClick = { prize, laureate ->
                    val p = URLEncoder.encode(gson.toJson(prize), "UTF-8")
                    val l = URLEncoder.encode(gson.toJson(laureate), "UTF-8")
                    navController.navigate("laureate_detail/$p/$l")
                }
            )
        }
        composable(
            "laureate_detail/{prize}/{laureate}",
            listOf(
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
            LaureateDetailScreen(prize, laureate, onBack = { navController.popBackStack() })
        }
    }
}