package com.example.kotlinuniversitykotlin6.presentation.users.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUserByIdUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    getUserByIdUseCase: GetUserByIdUseCase,
    logoutUseCase: LogoutUseCase,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: UserDetailViewModel = viewModel(
        key = userId.toString(),
        factory = UserDetailViewModel.Factory(userId, getUserByIdUseCase, logoutUseCase)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (val state = uiState) {
                is UserDetailUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is UserDetailUiState.Error ->
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = viewModel::loadUser) { Text("Повторить") }
                    }

                is UserDetailUiState.Success -> {
                    val user = state.user
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = user.imageUrl, contentDescription = user.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(user.fullName, style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "@${user.username}", style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                listOf(
                                    "Email" to user.email,
                                    "Телефон" to user.phone,
                                    "Город" to user.city,
                                    "Страна" to user.country,
                                    "Компания" to user.company
                                ).filter { it.second.isNotBlank() }.forEach { (label, value) ->
                                    Row(Modifier.padding(vertical = 6.dp)) {
                                        Text(
                                            "$label: ", fontWeight = FontWeight.Bold,
                                            modifier = Modifier.width(90.dp)
                                        )
                                        Text(value)
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        OutlinedButton(
                            onClick = { viewModel.logout(onLogout) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Выйти из аккаунта") }
                    }
                }
            }
        }
    }
}