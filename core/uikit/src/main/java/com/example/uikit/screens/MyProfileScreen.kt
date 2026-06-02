package com.example.uikit.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import com.example.uikit.screens.uikit.AddressCard
import com.example.uikit.screens.uikit.ChangePasswordDialog
import com.example.uikit.screens.uikit.DangerZone
import com.example.uikit.screens.uikit.DeleteAccountDialog
import com.example.uikit.screens.uikit.EditProfileDialog
import com.example.uikit.screens.uikit.ForgotPasswordDialog
import com.example.uikit.screens.uikit.EmptyState
import com.example.uikit.screens.uikit.LogoutConfirmDialog
import com.example.uikit.screens.uikit.MyContactsCard
import com.example.uikit.screens.uikit.MyProfileHeader
import com.example.uikit.screens.uikit.MyStatisticsCard
import com.example.uikit.screens.uikit.SettingsSection
import com.example.uikit.screens.viewmodel.MyProfileViewModel
import com.example.uikit.uikit.ClientBottomNavigation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    navController: NavHostController,
    viewModel: MyProfileViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    bottomBar: @Composable (NavHostController) -> Unit,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val client by viewModel.client.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.successMessage.collectAsState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Диалоги
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    // Наблюдаем за выходом
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onNavigateToAuth()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }
    LaunchedEffect(success) {
        success?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Мой профиль") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
         bottomBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading && client == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                client == null -> {
                    EmptyState(message = "Не удалось загрузить профиль")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Аватар + ФИО
                        item {
                            MyProfileHeader(
                                client = client!!,
                                onEditClick = { showEditDialog = true }
                            )
                        }

                        // Статистика
                        item {
                            MyStatisticsCard(
                                totalOrders = client!!.orderCount,
                                activeOrders = client!!.activeOrderCount,
                                totalSpent = client!!.totalSpent
                            )
                        }

                        // Контакты
                        item {
                            MyContactsCard(client = client!!)
                        }

                        // Адрес
                        if (client!!.address.isNotBlank()) {
                            item {
                                AddressCard(address = client!!.address)
                            }
                        }

                        // Настройки
                        item {
                            SettingsSection(
                                onEditProfile = { showEditDialog = true },
                                onChangePassword = { showChangePasswordDialog = true },
                                onForgotPassword = { showForgotPasswordDialog = true },
                                onEmailVerification = {
                                    viewModel.sendEmailVerification()
                                },
                                isEmailVerified = viewModel.isEmailVerified(),
                                email = viewModel.getCurrentEmail(),
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = onToggleDarkMode
                            )
                        }

                        // Опасная зона
                        item {
                            DangerZone(
                                onLogout = { showLogoutDialog = true },
                                onDeleteAccount = { showDeleteDialog = true }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    // Диалог выхода
    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    // Диалог удаления аккаунта
    if (showDeleteDialog) {
        DeleteAccountDialog(
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteAccount()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    // Диалог редактирования
    if (showEditDialog) {
        EditProfileDialog(
            client = client!!,
            onSave = { updatedClient ->
                viewModel.updateProfile(updatedClient)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    // Диалог смены пароля
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            currentEmail = viewModel.getCurrentEmail(),
            onChangePassword = { currentPassword, newPassword ->
                viewModel.changePassword(currentPassword, newPassword)
                showChangePasswordDialog = false
            },
            onDismiss = { showChangePasswordDialog = false }
        )
    }

    // Диалог восстановления пароля
    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            onSend = { email ->
                viewModel.sendPasswordResetEmail(email)
                showForgotPasswordDialog = false
            },
            onDismiss = { showForgotPasswordDialog = false }
        )
    }
}
