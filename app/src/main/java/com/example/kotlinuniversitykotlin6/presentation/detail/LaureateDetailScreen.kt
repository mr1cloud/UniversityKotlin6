package com.example.kotlinuniversitykotlin6.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import io.ktor.websocket.Frame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaureateDetailScreen(prize: NobelPrize, laureate: Laureate, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Frame.Text(laureate.fullName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            laureate.portraitUrl?.let {
                AsyncImage(
                    it, laureate.fullName, contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
            Column(Modifier.padding(16.dp)) {
                listOf(
                    "Имя" to laureate.fullName,
                    "Год" to prize.year,
                    "Категория" to prize.category.uppercase(),
                    "Страна" to laureate.country
                ).forEach { (label, value) ->
                    Row(Modifier.padding(vertical = 4.dp)) {
                        Text(
                            "$label: ", style = MaterialTheme.typography.bodyMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(value, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Мотивация:", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(laureate.motivation, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}