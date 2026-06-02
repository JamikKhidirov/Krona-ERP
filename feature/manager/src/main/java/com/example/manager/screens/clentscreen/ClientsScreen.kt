package com.example.manager.screens.clentscreen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import com.example.manager.screens.clentscreen.uikit.ClientCard
import com.example.manager.screens.clentscreen.uikit.EmptyClientsState
import com.example.manager.screens.clentscreen.uikit.EmptySearchState
import com.example.manager.screens.clentscreen.uikit.SearchBar
import com.example.manager.uikit.bottombar.ManagerBottomNavigation
import com.example.manager.screens.clentscreen.viewmodel.ClientsViewModel
import com.example.uikit.Client


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    onClientClick: (String) -> Unit,
    navController: NavHostController,
    viewModel: ClientsViewModel = hiltViewModel()
) {
    val clients by viewModel.clients.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "База клиентов",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            ManagerBottomNavigation(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Поиск
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                placeholder = "Поиск по имени или телефону"
            )

            // Список клиентов
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading && clients.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    clients.isEmpty() && searchQuery.isNotBlank() -> {
                        EmptySearchState(query = searchQuery)
                    }
                    clients.isEmpty() -> {
                        EmptyClientsState()
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = clients,
                                key = { it.id }
                            ) { client ->
                                ClientCard(
                                    client = client,
                                    onClick = { onClientClick(client.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun ClientsScreenPreview() {
    MaterialTheme {
        val previewClients = listOf(
            Client(
                id = "1",
                firstName = "Иван",
                lastName = "Иванов",
                middleName = "Иванович",
                phone = "+7 (900) 123-45-67",
                orderCount = 5,
                activeOrderCount = 2,
                furnitureTypes = listOf("Шкаф", "Кухня"),
                totalSpent = "350 000"
            ),
            Client(
                id = "2",
                firstName = "Анна",
                lastName = "Смирнова",
                phone = "+7 (916) 223-55-44",
                orderCount = 3,
                activeOrderCount = 0,
                furnitureTypes = listOf("Стол"),
                totalSpent = "120 000"
            ),
            Client(
                id = "3",
                firstName = "Алексей",
                lastName = "Петров",
                phone = "+7 (922) 555-66-77",
                orderCount = 1,
                activeOrderCount = 1,
                furnitureTypes = listOf("Стул"),
                totalSpent = "45 000"
            )
        )

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant)) {
            SearchBar(
                query = "",
                onQueryChange = {},
                placeholder = "Поиск по имени или телефону"
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(previewClients) { client ->
                    ClientCard(client = client, onClick = {})
                }
            }
        }
    }
}