package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import `in`.sitharaj.aurabudget.domain.model.BudgetEntity
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * ViewModel for budget management
 * Following MVVM pattern and clean architecture
 */
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        loadBudgets()
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            budgetRepository.getAllBudgets().collect { budgets ->
                _uiState.value = _uiState.value.copy(
                    budgets = budgets,
                    isLoading = false
                )
            }
        }
    }

    fun addBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            try {
                budgetRepository.insertBudget(budget)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            try {
                val budget = budgetRepository.getBudgetById(budgetId)
                budget?.let {
                    budgetRepository.deleteBudget(it)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            try {
                budgetRepository.updateBudget(budget)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}

data class BudgetUiState(
    val budgets: List<BudgetEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
