package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import `in`.sitharaj.aurabudget.domain.model.*
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * ViewModel for comprehensive analytics and financial insights
 */
@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            combine(
                expenseRepository.getAllExpenses(),
                budgetRepository.getAllBudgets(),
                getFinancialGoals(),
                getAccountsData()
            ) { expenses, budgets, goals, accounts ->
                
                val currentMonthExpenses = expenses.filter { expense ->
                    isCurrentMonth(expense.date)
                }
                
                val totalExpenses = currentMonthExpenses.sumOf { it.amount }
                val totalIncome = calculateTotalIncome(accounts)
                val netSavings = totalIncome - totalExpenses
                val savingsRate = if (totalIncome > 0) (netSavings / totalIncome) * 100 else 0.0
                
                // Group expenses by category
                val expensesByCategory = currentMonthExpenses
                    .groupBy { it.categoryId }
                    .map { (categoryId, expenses) ->
                        getCategoryName(categoryId) to expenses.sumOf { it.amount }
                    }
                    .sortedByDescending { it.second }
                
                // Calculate monthly trend (last 6 months)
                val monthlyTrend = calculateMonthlyTrend(expenses)
                
                // Calculate budget progress
                val budgetProgress = budgets.map { budget ->
                    val spent = currentMonthExpenses
                        .filter { it.categoryId == budget.categoryId }
                        .sumOf { it.amount }
                    
                    BudgetProgress(
                        name = getCategoryName(budget.categoryId ?: 0L),
                        spent = spent,
                        amount = budget.amount
                    )
                }
                
                // Generate AI insights
                val insights = generateFinancialInsights(
                    totalExpenses, totalIncome, expensesByCategory, budgetProgress
                )
                
                AnalyticsUiState(
                    totalIncome = totalIncome,
                    totalExpenses = totalExpenses,
                    netSavings = netSavings,
                    savingsRate = savingsRate,
                    expensesByCategory = expensesByCategory,
                    monthlyTrend = monthlyTrend,
                    budgetProgress = budgetProgress,
                    financialGoals = goals,
                    insights = insights,
                    isLoading = false
                )
                
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun isCurrentMonth(timestamp: Long): Boolean {
        // Implementation to check if timestamp is in current month
        val currentTime = System.currentTimeMillis()
        val thirtyDaysAgo = currentTime - (30 * 24 * 60 * 60 * 1000L)
        return timestamp >= thirtyDaysAgo
    }

    private fun calculateTotalIncome(accounts: List<Account>): Double {
        // Calculate total income from all sources
        return accounts.sumOf { it.balance.coerceAtLeast(0.0) }
    }

    private fun getCategoryName(categoryId: Long): String {
        // This should fetch from category repository
        return when (categoryId) {
            1L -> "Food & Dining"
            2L -> "Transportation"
            3L -> "Shopping"
            4L -> "Entertainment"
            5L -> "Bills & Utilities"
            6L -> "Healthcare"
            7L -> "Travel"
            8L -> "Education"
            else -> "Other"
        }
    }

    private fun calculateMonthlyTrend(expenses: List<ExpenseEntity>): List<Pair<String, Double>> {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
        return months.mapIndexed { index, month ->
            // Calculate expenses for each month (simplified)
            val monthlyExpenses = expenses.filter { 
                // Filter by month logic here
                true
            }.sumOf { it.amount }
            month to (monthlyExpenses + (index * 200.0)) // Mock data with trend
        }
    }

    private fun getFinancialGoals(): Flow<List<FinancialGoal>> = flow {
        emit(listOf(
            FinancialGoal(
                id = 1,
                name = "Emergency Fund",
                targetAmount = 10000.0,
                currentAmount = 6500.0,
                targetDate = LocalDate.parse("2025-12-31"),
                categoryId = null,
                description = "6 months of expenses"
            ),
            FinancialGoal(
                id = 2,
                name = "Vacation Fund",
                targetAmount = 3000.0,
                currentAmount = 1200.0,
                targetDate = LocalDate.parse("2025-07-01"),
                categoryId = 7L,
                description = "Summer vacation to Europe"
            ),
            FinancialGoal(
                id = 3,
                name = "New Car",
                targetAmount = 25000.0,
                currentAmount = 8500.0,
                targetDate = LocalDate.parse("2026-03-01"),
                categoryId = 2L,
                description = "Down payment for new car"
            )
        ))
    }

    private fun getAccountsData(): Flow<List<Account>> = flow {
        emit(listOf(
            Account(1, "Checking Account", Account.AccountType.BANK, 3500.0),
            Account(2, "Savings Account", Account.AccountType.SAVINGS, 12000.0),
            Account(3, "Credit Card", Account.AccountType.CREDIT_CARD, -850.0)
        ))
    }

    private fun generateFinancialInsights(
        totalExpenses: Double,
        totalIncome: Double,
        expensesByCategory: List<Pair<String, Double>>,
        budgetProgress: List<BudgetProgress>
    ): List<String> {
        val insights = mutableListOf<String>()
        
        // Savings rate insight
        val savingsRate = if (totalIncome > 0) (totalIncome - totalExpenses) / totalIncome * 100 else 0.0
        when {
            savingsRate >= 20 -> insights.add("Excellent! You're saving ${savingsRate.toInt()}% of your income. Keep up the great work!")
            savingsRate >= 10 -> insights.add("Good job! You're saving ${savingsRate.toInt()}% of your income. Consider increasing to 20% for better financial health.")
            else -> insights.add("Your savings rate is ${savingsRate.toInt()}%. Try to reduce expenses or increase income to save more.")
        }
        
        // Top spending category insight
        if (expensesByCategory.isNotEmpty()) {
            val topCategory = expensesByCategory.first()
            val percentage = (topCategory.second / totalExpenses * 100).toInt()
            insights.add("Your largest expense category is ${topCategory.first} at ${percentage}% of total spending.")
        }
        
        // Budget compliance insight
        val overBudgetCount = budgetProgress.count { it.spent > it.amount }
        if (overBudgetCount > 0) {
            insights.add("You're over budget in $overBudgetCount categories. Consider reviewing your spending habits.")
        } else {
            insights.add("Great! You're staying within budget across all categories this month.")
        }
        
        // Spending trend insight
        insights.add("Based on your spending patterns, consider setting up automatic savings to reach your financial goals faster.")
        
        return insights
    }

    fun refreshData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadAnalyticsData()
    }

    fun exportData() {
        viewModelScope.launch {
            // TODO: Implement data export functionality
        }
    }
}

/**
 * UI State for Analytics Screen
 */
data class AnalyticsUiState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netSavings: Double = 0.0,
    val savingsRate: Double = 0.0,
    val expensesByCategory: List<Pair<String, Double>> = emptyList(),
    val monthlyTrend: List<Pair<String, Double>> = emptyList(),
    val budgetProgress: List<BudgetProgress> = emptyList(),
    val financialGoals: List<FinancialGoal> = emptyList(),
    val insights: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * Budget progress data class
 */
data class BudgetProgress(
    val name: String,
    val spent: Double,
    val amount: Double
)
