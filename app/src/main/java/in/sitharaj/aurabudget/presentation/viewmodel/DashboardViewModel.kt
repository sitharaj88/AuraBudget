package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import `in`.sitharaj.aurabudget.domain.model.BudgetEntity
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import javax.inject.Inject

/**
 * Enhanced DashboardViewModel with financial analytics and insights
 * Following MVVM pattern and SOLID principles
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                budgetRepository.getAllBudgets(),
                expenseRepository.getAllExpenses()
            ) { budgets, expenses ->
                val recentExpenses = expenses.takeLast(5).reversed()
                val totalExpenses = expenses.sumOf { it.amount }
                val totalBudget = budgets.sumOf { it.amount }
                val totalSpent = budgets.sumOf { it.spent }

                // Calculate category spending
                val categorySpending = expenses.groupBy { it.categoryId }
                    .map { (categoryId, expenseList) ->
                        getCategoryName(categoryId) to expenseList.sumOf { it.amount }
                    }
                    .sortedByDescending { it.second }
                    .take(5)

                DashboardUiState(
                    budgets = budgets,
                    recentExpenses = recentExpenses,
                    totalExpenses = totalExpenses,
                    totalIncome = calculateTotalIncome(),
                    totalBudget = totalBudget,
                    totalSpent = totalSpent,
                    monthlyBudget = calculateMonthlyBudget(),
                    savingsRate = calculateSavingsRate(totalIncome = calculateTotalIncome(), totalExpenses = totalExpenses),
                    topCategories = categorySpending,
                    isLoading = false
                )
            }.catch { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun getCategoryName(categoryId: Long): String {
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

    private fun calculateTotalIncome(): Double {
        // This would normally come from income tracking
        // For now, return a mock value
        return 5000.0
    }

    private fun calculateMonthlyBudget(): Double {
        // This would normally be calculated based on user settings or historical data
        // For now, return a mock value
        return 2000.0
    }

    private fun calculateSavingsRate(totalIncome: Double, totalExpenses: Double): Double {
        return if (totalIncome > 0) {
            ((totalIncome - totalExpenses) / totalIncome) * 100
        } else {
            0.0
        }
    }

    fun refreshData() {
        loadDashboardData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class DashboardUiState(
    val budgets: List<BudgetEntity> = emptyList(),
    val recentExpenses: List<ExpenseEntity> = emptyList(),
    val totalExpenses: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val monthlyBudget: Double = 0.0,
    val savingsRate: Double = 0.0,
    val topCategories: List<Pair<String, Double>> = emptyList(),
    val monthlyTrend: List<Pair<String, Double>> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
