package com.example.kotlinuniversitykotlin6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinuniversitykotlin6.data.remote.RetrofitClient
import com.example.kotlinuniversitykotlin6.domain.repository.PhotoRepositoryImpl
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPhotosUseCase
import com.example.kotlinuniversitykotlin6.navigation.AppNavGraph
import com.example.kotlinuniversitykotlin6.ui.theme.KotlinUniversityKotlin6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinUniversityKotlin6Theme {
                AppNavGraph(
                    getPhotosUseCase = GetPhotosUseCase(
                        PhotoRepositoryImpl(RetrofitClient.picsumApi)
                    )
                )
            }
        }
    }
}
