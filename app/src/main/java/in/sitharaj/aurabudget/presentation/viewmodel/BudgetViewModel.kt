package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.sitharaj.aurabudget.domain.model.BudgetEntity
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for budget management
 * Following MVVM pattern and clean architecture
 */
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    val budgets = budgetRepository.getAllBudgets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            budgetRepository.insertBudget(budget)
        }
    }

    fun deleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }
}
