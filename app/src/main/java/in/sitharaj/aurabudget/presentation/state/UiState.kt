package `in`.sitharaj.aurabudget.presentation.state

/**
 * UI State for Expense screen following MVVM pattern
 * Represents all possible UI states for expense management
 */
data class ExpenseUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isExpenseAdded: Boolean = false,
    val searchQuery: String = "",
    val selectedCategoryId: Long? = null,
    val isDialogVisible: Boolean = false,
    val expenseToEdit: `in`.sitharaj.aurabudget.domain.model.ExpenseEntity? = null
)

/**
 * UI State for Dashboard with financial insights
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalExpenses: Double = 0.0,
    val totalIncome: Double = 0.0,
    val monthlyBudget: Double = 0.0,
    val savingsRate: Double = 0.0,
    val topCategories: List<Pair<String, Double>> = emptyList(),
    val recentExpenses: List<`in`.sitharaj.aurabudget.domain.model.ExpenseEntity> = emptyList(),
    val monthlyTrend: List<`in`.sitharaj.aurabudget.domain.model.MonthlyData> = emptyList()
)
