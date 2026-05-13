package com.example.kotlinuniversitykotlin6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinuniversitykotlin6.navigation.AppNavGraph
import com.example.kotlinuniversitykotlin6.ui.theme.KotlinUniversityKotlin6Theme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as NobelApp
        val startRoute = runBlocking {
            if (app.tokenDataStore.getToken() != null) "prizes" else "login"
        }

        enableEdgeToEdge()
        setContent {
            KotlinUniversityKotlin6Theme {
                AppNavGraph(app, startRoute)
            }
        }
    }
}
