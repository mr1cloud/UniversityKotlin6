package com.example.kotlinuniversitykotlin6.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinuniversitykotlin6.domain.model.Laureate
import com.example.kotlinuniversitykotlin6.domain.model.NobelPrize
import com.example.kotlinuniversitykotlin6.domain.usecase.AddFavoriteUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.GetPrizesUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.LogoutUseCase
import com.example.kotlinuniversitykotlin6.domain.usecase.RemoveFavoriteUseCase
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NobelListScreen(
    getPrizesUseCase: GetPrizesUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    removeFavoriteUseCase: RemoveFavoriteUseCase,
    logoutUseCase: LogoutUseCase,
    onLaureateClick: (NobelPrize, Laureate) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: NobelListViewModel = viewModel(
        factory = NobelListViewModel.Factory(
            getPrizesUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase,
            logoutUseCase
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    var yearExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    val years = (1901..Calendar.getInstance().get(Calendar.YEAR)).toList().reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Нобелевские лауреаты") },
                actions = {
                    TextButton(onClick = { viewModel.logout(onLogout) }) {
                        Text("Выйти", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { yearExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.selectedYear?.toString() ?: "Год")
                    }
                    DropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все годы") },
                            onClick = {
                                yearExpanded = false
                                viewModel.loadPrizes(year = null)
                            }
                        )
                        years.take(30).forEach { y ->
                            DropdownMenuItem(
                                text = { Text(y.toString()) },
                                onClick = {
                                    yearExpanded = false
                                    viewModel.loadPrizes(year = y)
                                }
                            )
                        }
                    }
                }

                Box(Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { categoryExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            viewModel.selectedCategory
                                ?.replaceFirstChar { it.uppercase() }
                                ?: "Категория"
                        )
                    }
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все категории") },
                            onClick = {
                                categoryExpanded = false
                                viewModel.loadPrizes(category = null)
                            }
                        )
                        viewModel.categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    categoryExpanded = false
                                    viewModel.loadPrizes(category = cat)
                                }
                            )
                        }
                    }
                }
            }

            Box(Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is NobelListUiState.Loading ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    is NobelListUiState.Error ->
                        Column(
                            Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                state.message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(onClick = { viewModel.loadPrizes() }) {
                                Text("Повторить")
                            }
                        }

                    is NobelListUiState.Success ->
                        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                            state.prizes.forEach { prize ->
                                prize.laureates.forEach { laureate ->
                                    item(key = "${prize.awardYear}-${laureate.id}") {
                                        Card(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                                .clickable { onLaureateClick(prize, laureate) }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(Modifier.weight(1f)) {
                                                    Text(
                                                        "${prize.awardYear} · ${prize.category.uppercase()}",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        laureate.fullName,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                    laureate.motivation?.let { motivation ->
                                                        Text(
                                                            motivation.take(100) +
                                                                    if (motivation.length > 100) "…" else "",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            maxLines = 2,
                                                            overflow = TextOverflow.Ellipsis,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }

                                                IconButton(
                                                    onClick = { viewModel.toggleFavorite(prize) }
                                                ) {
                                                    Icon(
                                                        imageVector = if (prize.id in favoriteIds)
                                                            Icons.Default.Favorite
                                                        else
                                                            Icons.Default.FavoriteBorder,
                                                        contentDescription = "Избранное",
                                                        tint = if (prize.id in favoriteIds)
                                                            MaterialTheme.colorScheme.error
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}