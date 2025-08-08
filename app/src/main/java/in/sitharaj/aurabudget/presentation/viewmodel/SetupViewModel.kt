package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import `in`.sitharaj.aurabudget.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * ViewModel for the Setup Screen - Handles initial app configuration
 */
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updateCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(currency = currency)
    }

    fun updateMonthlyIncome(income: String) {
        _uiState.value = _uiState.value.copy(monthlyIncome = income)
    }

    fun toggleCategory(categoryName: String) {
        val currentCategories = _uiState.value.selectedCategories.toMutableList()
        if (currentCategories.contains(categoryName)) {
            currentCategories.remove(categoryName)
        } else {
            currentCategories.add(categoryName)
        }
        _uiState.value = _uiState.value.copy(selectedCategories = currentCategories)
    }

    fun updateCategoryBudget(category: String, budget: String) {
        val currentBudgets = _uiState.value.categoryBudgets.toMutableMap()
        currentBudgets[category] = budget
        _uiState.value = _uiState.value.copy(categoryBudgets = currentBudgets)
    }

    fun isStepValid(step: Int): Boolean {
        return when (step) {
            0 -> _uiState.value.name.isNotBlank()
            1 -> _uiState.value.currency.isNotBlank() && _uiState.value.monthlyIncome.isNotBlank()
            2 -> _uiState.value.selectedCategories.isNotEmpty()
            3 -> true // Budget step is optional
            else -> false
        }
    }

    fun completeSetup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Save user preferences
                // Create default categories
                // Set up initial budgets
                // Navigate to main app

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}

data class SetupUiState(
    val name: String = "",
    val email: String = "",
    val currency: String = "USD",
    val monthlyIncome: String = "",
    val selectedCategories: List<String> = emptyList(),
    val categoryBudgets: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)
