package `in`.sitharaj.aurabudget.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.viewmodel.SettingsViewModel

/**
 * Settings Screen - App preferences and user configuration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Profile Section
            item {
                ProfileSection(
                    userName = uiState.userName,
                    userEmail = uiState.userEmail,
                    onEditProfile = { navController.navigate("edit_profile") }
                )
            }

            item {
                SectionDivider()
            }

            // App Preferences
            item {
                SectionHeader("App Preferences")
            }

            items(getAppPreferenceItems(uiState, viewModel)) { item ->
                SettingsItem(item = item)
            }

            item {
                SectionDivider()
            }

            // Financial Settings
            item {
                SectionHeader("Financial Settings")
            }

            items(getFinancialSettingsItems(navController)) { item ->
                SettingsItem(item = item)
            }

            item {
                SectionDivider()
            }

            // Data & Privacy
            item {
                SectionHeader("Data & Privacy")
            }

            items(getDataPrivacyItems(viewModel, navController)) { item ->
                SettingsItem(item = item)
            }

            item {
                SectionDivider()
            }

            // About & Support
            item {
                SectionHeader("About & Support")
            }

            items(getAboutSupportItems(navController)) { item ->
                SettingsItem(item = item)
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ProfileSection(
    userName: String,
    userEmail: String,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onEditProfile
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (userName.isNotBlank()) userName.first().uppercase() else "U",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName.ifBlank { "User Name" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = userEmail.ifBlank { "user@example.com" },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Edit Profile",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
}

@Composable
fun SettingsItem(
    item: SettingsItemData,
    modifier: Modifier = Modifier
) {
    when (item.type) {
        SettingsItemType.SWITCH -> {
            SwitchSettingsItem(
                icon = item.icon,
                title = item.title,
                subtitle = item.subtitle,
                checked = item.isChecked ?: false,
                onCheckedChange = item.onToggle ?: {},
                modifier = modifier
            )
        }

        SettingsItemType.NAVIGATION -> {
            NavigationSettingsItem(
                icon = item.icon,
                title = item.title,
                subtitle = item.subtitle,
                onClick = item.onClick ?: {},
                modifier = modifier
            )
        }

        SettingsItemType.ACTION -> {
            ActionSettingsItem(
                icon = item.icon,
                title = item.title,
                subtitle = item.subtitle,
                onClick = item.onClick ?: {},
                isDestructive = item.isDestructive ?: false,
                modifier = modifier
            )
        }
    }
}

@Composable
fun SwitchSettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                subtitle?.let { sub ->
                    Text(
                        text = sub,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun NavigationSettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                subtitle?.let { sub ->
                    Text(
                        text = sub,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ActionSettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )

                subtitle?.let { sub ->
                    Text(
                        text = sub,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Helper functions to create settings items
fun getAppPreferenceItems(
    uiState: `in`.sitharaj.aurabudget.presentation.viewmodel.SettingsUiState,
    viewModel: SettingsViewModel
): List<SettingsItemData> {
    return listOf(
        SettingsItemData(
            type = SettingsItemType.SWITCH,
            icon = Icons.Default.DarkMode,
            title = "Dark Mode",
            subtitle = "Switch between light and dark themes",
            isChecked = uiState.isDarkMode,
            onToggle = { viewModel.toggleDarkMode() }
        ),
        SettingsItemData(
            type = SettingsItemType.SWITCH,
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Receive budget alerts and reminders",
            isChecked = uiState.notificationsEnabled,
            onToggle = { viewModel.toggleNotifications() }
        ),
        SettingsItemData(
            type = SettingsItemType.SWITCH,
            icon = Icons.Default.Security,
            title = "Biometric Lock",
            subtitle = "Use fingerprint or face to unlock",
            isChecked = uiState.biometricEnabled,
            onToggle = { viewModel.toggleBiometric() }
        )
    )
}

fun getFinancialSettingsItems(navController: NavController): List<SettingsItemData> {
    return listOf(
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.AttachMoney,
            title = "Currency",
            subtitle = "USD - United States Dollar",
            onClick = { navController.navigate("currency_settings") }
        ),
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.Category,
            title = "Categories",
            subtitle = "Manage expense categories",
            onClick = { navController.navigate("categories") }
        ),
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.AccountBalance,
            title = "Accounts",
            subtitle = "Manage bank accounts and cards",
            onClick = { navController.navigate("accounts") }
        ))

}

fun getDataPrivacyItems(
    viewModel: SettingsViewModel,
    navController: NavController
): List<SettingsItemData> {
    return listOf(
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.CloudUpload,
            title = "Backup & Sync",
            subtitle = "Secure cloud backup",
            onClick = { navController.navigate("backup_settings") }
        ),
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.FileDownload,
            title = "Export Data",
            subtitle = "Download your financial data",
            onClick = { viewModel.exportData() }
        ),
        SettingsItemData(
            type = SettingsItemType.ACTION,
            icon = Icons.Default.DeleteForever,
            title = "Clear All Data",
            subtitle = "Permanently delete all your data",
            onClick = { viewModel.showClearDataDialog() },
            isDestructive = true
        )
    )
}


fun getAboutSupportItems(navController: NavController): List<SettingsItemData> {
    return listOf(
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.AutoMirrored.Filled.Help,
            title = "Help & Support",
            subtitle = "Get help and contact support",
            onClick = { navController.navigate("help") }
        ),
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.Info,
            title = "About AuraBudget",
            subtitle = "Version 1.0.0",
            onClick = { navController.navigate("about") }
        ),
        SettingsItemData(
            type = SettingsItemType.NAVIGATION,
            icon = Icons.Default.Star,
            title = "Rate App",
            subtitle = "Rate us on the Play Store",
            onClick = { /* Open Play Store */ }
        ))
}

// Data classes
data class SettingsItemData(
    val type: SettingsItemType,
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val isChecked: Boolean? = null,
    val onToggle: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
    val isDestructive: Boolean? = null
)

enum class SettingsItemType {
    SWITCH, NAVIGATION, ACTION
}
