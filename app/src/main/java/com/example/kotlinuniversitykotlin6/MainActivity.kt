package com.example.kotlinuniversitykotlin6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinuniversitykotlin6.ui.theme.KotlinUniversityKotlin6Theme
import com.example.kotlinuniversitykotlin6.presentation.BleScannerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinUniversityKotlin6Theme {
                BleScannerScreen()
            }
        }
    }
}
