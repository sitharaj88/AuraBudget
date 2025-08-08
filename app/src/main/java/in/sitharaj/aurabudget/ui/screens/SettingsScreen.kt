package `in`.sitharaj.aurabudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import `in`.sitharaj.aurabudget.presentation.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Appearance",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Dark Theme",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isDarkTheme) "Enabled" else "Disabled",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { themeViewModel.toggleTheme() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "About",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "AuraBudget",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Version 1.0",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Advanced finance and money management app designed to empower users to effortlessly track expenses, set budgets, and reach their financial goals with confidence.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Features",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val features = listOf(
                        "• Modern MVVM Clean Architecture",
                        "• Elegant, Responsive UI",
                        "• Instant Theme Switching",
                        "• Real-Time Insights",
                        "• Secure Data Handling",
                        "• Smart Expense Tracking"
                    )

                    features.forEach { feature ->
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
