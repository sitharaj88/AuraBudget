package `in`.sitharaj.aurabudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import `in`.sitharaj.aurabudget.presentation.navigation.MainNavigationBar
import `in`.sitharaj.aurabudget.presentation.navigation.Screen
import `in`.sitharaj.aurabudget.presentation.screen.*
import `in`.sitharaj.aurabudget.ui.screens.SplashScreen
import `in`.sitharaj.aurabudget.ui.theme.AuraBudgetTheme
import `in`.sitharaj.aurabudget.presentation.viewmodel.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuraBudgetScreen()
        }
    }
}

@Composable
fun AuraBudgetScreen() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    AuraBudgetTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                MainNavigationBar(navController = navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                // Splash and Onboarding
                composable(Screen.Splash.route) {
                    SplashScreen(onNavigateToMain = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    })
                }

                // Main Navigation Screens
                composable(Screen.Dashboard.route) {
                    EnhancedDashboardScreen(navController = navController)
                }

                composable(Screen.Transactions.route) {
                    TransactionsScreen(navController = navController)
                }

                composable(Screen.Budget.route) {
                    BudgetScreen(navController = navController)
                }

                composable(Screen.Analytics.route) {
                    AnalyticsScreen(navController = navController)
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(navController = navController)
                }

                // Secondary Screens
                composable(
                    route = "${Screen.AddTransaction.route}?type={type}",
                    arguments = listOf(navArgument("type") {
                        type = NavType.StringType
                        defaultValue = "expense"
                    })
                ) { backStackEntry ->
                    val transactionType = backStackEntry.arguments?.getString("type") ?: "expense"
                    AddTransactionScreen(
                        navController = navController,
                        transactionType = transactionType
                    )
                }

                composable(Screen.AddTransaction.route) {
                    AddTransactionScreen(navController = navController)
                }

                // Transaction Detail - placeholder for now
                composable(
                    route = Screen.TransactionDetail.route,
                    arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
                ) {
                    // TransactionDetailScreen placeholder - will implement later
                    Box {}
                }

                // Budget related screens - placeholders for now
                composable(Screen.AddBudget.route) {
                    // AddBudgetScreen placeholder - will implement later
                    Box {}
                }

                composable(
                    route = Screen.BudgetDetail.route,
                    arguments = listOf(navArgument("budgetId") { type = NavType.LongType })
                ) {
                    // BudgetDetailScreen placeholder - will implement later
                    Box {}
                }

                // Categories and Goals - placeholders for now
                composable(Screen.Categories.route) {
                    // CategoriesScreen placeholder - will implement later
                    Box {}
                }

                composable(Screen.Goals.route) {
                    // GoalsScreen placeholder - will implement later
                    Box {}
                }

                // Profile and Settings related - placeholders for now
                composable(Screen.Profile.route) {
                    // ProfileScreen placeholder - will implement later
                    Box {}
                }

                composable(Screen.Notifications.route) {
                    // NotificationsScreen placeholder - will implement later
                    Box {}
                }

                composable(Screen.Help.route) {
                    // HelpScreen placeholder - will implement later
                    Box {}
                }

                composable(Screen.About.route) {
                    // AboutScreen placeholder - will implement later
                    Box {}
                }
            }
        }
    }
}