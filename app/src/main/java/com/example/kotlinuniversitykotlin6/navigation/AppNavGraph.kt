package com.example.kotlinuniversitykotlin6.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinuniversitykotlin6.domain.model.Photo
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPhotosUseCase
import com.example.kotlinuniversitykotlin6.presentation.detail.PhotoDetailScreen
import com.example.kotlinuniversitykotlin6.presentation.list.PhotoListScreen
import com.google.gson.Gson

@Composable
fun AppNavGraph(getPhotosUseCase: GetPhotosUseCase) {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(navController, startDestination = "photo_list") {
        composable("photo_list") {
            PhotoListScreen(
                getPhotosUseCase = getPhotosUseCase,
                onPhotoClick = { photo ->
                    val json = Uri.encode(gson.toJson(photo))
                    navController.navigate("photo_detail/$json")
                }
            )
        }
        composable(
            "photo_detail/{photo}",
            listOf(navArgument("photo") { type = NavType.StringType })
        ) { back ->
            val photo = gson.fromJson(
                Uri.decode(back.arguments?.getString("photo")), Photo::class.java
            )
            PhotoDetailScreen(photo = photo, onBack = { navController.popBackStack() })
        }
    }
}