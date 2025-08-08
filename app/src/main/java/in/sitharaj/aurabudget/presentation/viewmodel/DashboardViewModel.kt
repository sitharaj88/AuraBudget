package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.sitharaj.aurabudget.domain.usecase.analytics.GetFinancialAnalyticsUseCase
import `in`.sitharaj.aurabudget.domain.usecase.expense.GetExpensesUseCase
import `in`.sitharaj.aurabudget.presentation.state.DashboardUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Enhanced DashboardViewModel with financial analytics and insights
 * Following MVVM pattern and SOLID principles
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getFinancialAnalyticsUseCase: GetFinancialAnalyticsUseCase,
    private val getExpensesUseCase: GetExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            combine(
                getFinancialAnalyticsUseCase.getFinancialSummary(),
                getExpensesUseCase().map { it.take(5) }
            ) { financialSummary, recentExpenses ->
                DashboardUiState(
                    isLoading = false,
                    totalExpenses = financialSummary.totalExpenses,
                    totalIncome = financialSummary.totalIncome,
                    savingsRate = financialSummary.getSavingsRate(),
                    topCategories = financialSummary.topCategories.map { (category, amount) ->
                        category.name to amount
                    },
                    recentExpenses = recentExpenses,
                    monthlyTrend = financialSummary.monthlyTrend
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

    fun refreshData() {
        loadDashboardData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
