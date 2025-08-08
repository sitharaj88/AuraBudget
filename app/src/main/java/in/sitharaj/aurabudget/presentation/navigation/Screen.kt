package `in`.sitharaj.aurabudget.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    // Main Navigation Screens
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Transactions : Screen("transactions", "Transactions", Icons.Default.Receipt)
    object Budget : Screen("budget", "Budget", Icons.Default.AccountBalance)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Analytics)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Secondary Screens
    object Splash : Screen("splash", "Splash")
    object Onboarding : Screen("onboarding", "Onboarding")
    object AddTransaction : Screen("add_transaction", "Add Transaction")
    object TransactionDetail : Screen("transaction_detail/{transactionId}", "Transaction Detail") {
        fun createRoute(transactionId: Long) = "transaction_detail/$transactionId"
    }
    object AddBudget : Screen("add_budget", "Add Budget")
    object BudgetDetail : Screen("budget_detail/{budgetId}", "Budget Detail") {
        fun createRoute(budgetId: Long) = "budget_detail/$budgetId"
    }
    object Categories : Screen("categories", "Categories")
    object AddCategory : Screen("add_category", "Add Category")
    object Goals : Screen("goals", "Goals")
    object AddGoal : Screen("add_goal", "Add Goal")
    object Profile : Screen("profile", "Profile")
    object Notifications : Screen("notifications", "Notifications")
    object Help : Screen("help", "Help")
    object About : Screen("about", "About")

    companion object {
        val bottomNavItems = listOf(Dashboard, Transactions, Budget, Analytics, Settings)
    }
}
