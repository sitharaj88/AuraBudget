package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.SectionHeader
import `in`.sitharaj.aurabudget.presentation.navigation.Screen

data class SettingsItem(
    val title: String,
    val subtitle: String?,
    val icon: ImageVector,
    val action: () -> Unit,
    val showSwitch: Boolean = false,
    val switchState: Boolean = false,
    val onSwitchChanged: ((Boolean) -> Unit)? = null,
    val showArrow: Boolean = true,
    val color: Color? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isDarkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Profile.route) }
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "John Doe",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "john.doe@email.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // App Preferences
            item {
                SettingsSection(
                    title = "App Preferences",
                    items = listOf(
                        SettingsItem(
                            title = "Dark Mode",
                            subtitle = "Toggle dark theme",
                            icon = Icons.Default.DarkMode,
                            action = { },
                            showSwitch = true,
                            switchState = isDarkMode,
                            onSwitchChanged = { isDarkMode = it },
                            showArrow = false
                        ),
                        SettingsItem(
                            title = "Notifications",
                            subtitle = "Manage app notifications",
                            icon = Icons.Default.Notifications,
                            action = { navController.navigate(Screen.Notifications.route) },
                            showSwitch = true,
                            switchState = notificationsEnabled,
                            onSwitchChanged = { notificationsEnabled = it }
                        ),
                        SettingsItem(
                            title = "Currency",
                            subtitle = "Indian Rupee (INR)",
                            icon = Icons.Default.CurrencyExchange,
                            action = { /* Navigate to currency settings */ }
                        ),
                        SettingsItem(
                            title = "Language",
                            subtitle = "English",
                            icon = Icons.Default.Language,
                            action = { /* Navigate to language settings */ }
                        )
                    )
                )
            }

            // Security & Privacy
            item {
                SettingsSection(
                    title = "Security & Privacy",
                    items = listOf(
                        SettingsItem(
                            title = "Biometric Authentication",
                            subtitle = "Use fingerprint or face unlock",
                            icon = Icons.Default.Fingerprint,
                            action = { },
                            showSwitch = true,
                            switchState = biometricEnabled,
                            onSwitchChanged = { biometricEnabled = it },
                            showArrow = false
                        ),
                        SettingsItem(
                            title = "PIN Setup",
                            subtitle = "Set up app PIN",
                            icon = Icons.Default.Lock,
                            action = { /* Navigate to PIN setup */ }
                        ),
                        SettingsItem(
                            title = "Privacy Policy",
                            subtitle = "Read our privacy policy",
                            icon = Icons.Default.PrivacyTip,
                            action = { /* Navigate to privacy policy */ }
                        )
                    )
                )
            }

            // Data Management
            item {
                SettingsSection(
                    title = "Data Management",
                    items = listOf(
                        SettingsItem(
                            title = "Export Data",
                            subtitle = "Download your financial data",
                            icon = Icons.Default.FileDownload,
                            action = { /* Export data functionality */ }
                        ),
                        SettingsItem(
                            title = "Import Data",
                            subtitle = "Import from CSV or other apps",
                            icon = Icons.Default.FileUpload,
                            action = { /* Import data functionality */ }
                        ),
                        SettingsItem(
                            title = "Backup & Sync",
                            subtitle = "Cloud backup settings",
                            icon = Icons.Default.CloudSync,
                            action = { /* Navigate to backup settings */ }
                        ),
                        SettingsItem(
                            title = "Clear Cache",
                            subtitle = "Clear app cache data",
                            icon = Icons.Default.CleaningServices,
                            action = { /* Clear cache */ }
                        )
                    )
                )
            }

            // Support & Help
            item {
                SettingsSection(
                    title = "Support & Help",
                    items = listOf(
                        SettingsItem(
                            title = "Help Center",
                            subtitle = "Get help and support",
                            icon = Icons.Default.Help,
                            action = { navController.navigate(Screen.Help.route) }
                        ),
                        SettingsItem(
                            title = "Contact Support",
                            subtitle = "Reach out to our team",
                            icon = Icons.Default.Support,
                            action = { /* Contact support */ }
                        ),
                        SettingsItem(
                            title = "Rate App",
                            subtitle = "Rate us on Play Store",
                            icon = Icons.Default.Star,
                            action = { /* Open Play Store */ }
                        ),
                        SettingsItem(
                            title = "About",
                            subtitle = "App version and info",
                            icon = Icons.Default.Info,
                            action = { navController.navigate(Screen.About.route) }
                        )
                    )
                )
            }

            // Danger Zone
            item {
                SettingsSection(
                    title = "Danger Zone",
                    items = listOf(
                        SettingsItem(
                            title = "Reset All Data",
                            subtitle = "Clear all app data",
                            icon = Icons.Default.DeleteForever,
                            action = { /* Show confirmation dialog */ },
                            color = Color.Red
                        ),
                        SettingsItem(
                            title = "Delete Account",
                            subtitle = "Permanently delete your account",
                            icon = Icons.Default.PersonRemove,
                            action = { /* Show confirmation dialog */ },
                            color = Color.Red
                        )
                    )
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionHeader(title = title)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsItemRow(item = item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { item.action() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    (item.color ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.color ?: MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = item.color ?: MaterialTheme.colorScheme.onSurface
            )
            if (item.subtitle != null) {
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (item.showSwitch && item.onSwitchChanged != null) {
            Switch(
                checked = item.switchState,
                onCheckedChange = item.onSwitchChanged
            )
        } else if (item.showArrow) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
