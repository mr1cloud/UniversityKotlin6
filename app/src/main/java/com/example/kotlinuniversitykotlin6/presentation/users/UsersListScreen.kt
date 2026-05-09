package com.example.kotlinuniversitykotlin6.presentation.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinuniversitykotlin6.domain.model.User
import com.example.kotlinuniversitykotlin6.domain.usecase.GetUsersUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    getUsersUseCase: GetUsersUseCase,
    logoutUseCase: LogoutUseCase,
    onUserClick: (User) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: UsersViewModel = viewModel(
        factory = UsersViewModel.Factory(getUsersUseCase, logoutUseCase)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    TextButton(onClick = { viewModel.logout(onLogout) }) {
                        Text("Выйти", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (val state = uiState) {
                is UsersUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is UsersUiState.Error ->
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = viewModel::loadUsers) { Text("Повторить") }
                    }

                is UsersUiState.Success ->
                    LazyColumn {
                        items(state.users, key = { it.id }) { user ->
                            ListItem(
                                modifier = Modifier.clickable { onUserClick(user) },
                                leadingContent = {
                                    AsyncImage(
                                        model = user.imageUrl,
                                        contentDescription = user.fullName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                    )
                                },
                                headlineContent = { Text(user.fullName) },
                                supportingContent = {
                                    Column {
                                        Text(
                                            "@${user.username}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            user.email,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            )
                            HorizontalDivider()
                        }
                    }
            }
        }
    }
}