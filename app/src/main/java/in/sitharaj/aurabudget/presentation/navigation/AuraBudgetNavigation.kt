package `in`.sitharaj.aurabudget.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.sitharaj.aurabudget.presentation.screen.EnhancedDashboardScreen
import `in`.sitharaj.aurabudget.presentation.screen.EnhancedExpenseScreen
import `in`.sitharaj.aurabudget.presentation.screen.AnalyticsScreen
import `in`.sitharaj.aurabudget.presentation.screen.onboarding.OnboardingScreen
import `in`.sitharaj.aurabudget.presentation.screen.setup.SetupScreen
import `in`.sitharaj.aurabudget.presentation.screen.budget.BudgetScreen
import `in`.sitharaj.aurabudget.presentation.screen.goals.GoalsScreen
import `in`.sitharaj.aurabudget.presentation.screen.settings.SettingsScreen
import `in`.sitharaj.aurabudget.presentation.screen.categories.CategoriesScreen

/**
 * Main Navigation Component for AuraBudget
 */
@Composable
fun AuraBudgetNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        // Onboarding Flow
        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("setup") {
            SetupScreen(navController)
        }

        // Main App Flow
        composable("main") {
            MainScreenWithBottomNav(navController)
        }

        // Individual Screens accessible from bottom nav
        composable("dashboard") {
            MainScreenWithBottomNav(navController)
        }

        // Settings and other screens
        composable("settings") {
            SettingsScreen(navController)
        }

        composable("categories") {
            CategoriesScreen(navController)
        }

        // Additional screens can be added here
        composable("profile") {
            ProfileScreen(navController)
        }

        composable("accounts") {
            AccountsScreen(navController)
        }

        composable("reports") {
            ReportsScreen(navController)
        }
    }
}

@Composable
fun MainScreenWithBottomNav(
    mainNavController: NavHostController
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Dashboard.route) {
                EnhancedDashboardScreen(bottomNavController)
            }

            composable(BottomNavItem.Expenses.route) {
                EnhancedExpenseScreen(bottomNavController)
            }

            composable(BottomNavItem.Budget.route) {
                BudgetScreen(bottomNavController)
            }

            composable(BottomNavItem.Goals.route) {
                GoalsScreen(bottomNavController)
            }

            composable(BottomNavItem.Analytics.route) {
                AnalyticsScreen(bottomNavController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Expenses,
        BottomNavItem.Budget,
        BottomNavItem.Goals,
        BottomNavItem.Analytics
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Expenses : BottomNavItem("expenses", "Expenses", Icons.Default.Receipt)
    object Budget : BottomNavItem("budget", "Budget", Icons.Default.AccountBalance)
    object Goals : BottomNavItem("goals", "Goals", Icons.Default.EmojiEvents)
    object Analytics : BottomNavItem("analytics", "Analytics", Icons.Default.Analytics)
}

// Placeholder screens that need to be implemented
@Composable
fun ProfileScreen(navController: NavHostController) {
    BasicScreen("Profile", "Manage your profile and personal information", navController)
}

@Composable
fun AccountsScreen(navController: NavHostController) {
    BasicScreen("Accounts", "Manage your bank accounts and payment methods", navController)
}

@Composable
fun ReportsScreen(navController: NavHostController) {
    BasicScreen("Reports", "Detailed financial reports and insights", navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(
    title: String,
    description: String,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Coming Soon!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
