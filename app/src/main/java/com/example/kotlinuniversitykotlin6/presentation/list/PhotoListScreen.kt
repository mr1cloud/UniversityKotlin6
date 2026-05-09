package com.example.kotlinuniversitykotlin6.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinuniversitykotlin6.domain.model.Photo
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPhotosUseCase

@Composable
fun PhotoListScreen(
    getPhotosUseCase: GetPhotosUseCase,
    onPhotoClick: (Photo) -> Unit
) {
    val viewModel: PhotoListViewModel = viewModel(
        factory = PhotoListViewModel.Factory(getPhotosUseCase)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold() { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is PhotoListUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is PhotoListUiState.Error ->
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            state.message, color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = viewModel::loadPhotos) { Text("Повторить") }
                    }

                is PhotoListUiState.Success ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.photos) { photo ->
                            PhotoCard(photo, onClick = { onPhotoClick(photo) })
                        }
                    }
            }
        }
    }
}