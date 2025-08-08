package `in`.sitharaj.aurabudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.sitharaj.aurabudget.presentation.screen.EnhancedDashboardScreen
import `in`.sitharaj.aurabudget.presentation.screen.EnhancedExpenseScreen
import `in`.sitharaj.aurabudget.ui.screens.SettingsScreen
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

        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(onNavigateToMain = {
                    navController.navigate("dashboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("dashboard") {
                EnhancedDashboardScreen(navController = navController)
            }
            composable("expenses") {
                EnhancedExpenseScreen(navController = navController)
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}