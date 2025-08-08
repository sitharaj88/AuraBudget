package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.usecase.expense.AddExpenseUseCase
import `in`.sitharaj.aurabudget.domain.usecase.expense.DeleteExpenseUseCase
import `in`.sitharaj.aurabudget.domain.usecase.expense.GetExpensesUseCase
import `in`.sitharaj.aurabudget.presentation.state.ExpenseUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Enhanced ExpenseViewModel following MVVM pattern and SOLID principles
 * Handles UI state management and business logic coordination
 */
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    val expenses = getExpensesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addExpense(
        amount: Double,
        categoryId: Long,
        description: String,
        tags: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            val expense = ExpenseEntity(
                amount = amount,
                categoryId = categoryId,
                date = System.currentTimeMillis(),
                description = description,
                tags = tags
            )

            addExpenseUseCase(expense)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isExpenseAdded = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            deleteExpenseUseCase(expense)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>> {
        return getExpensesUseCase.getByCategory(categoryId)
    }

    fun searchExpenses(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetExpenseAddedState() {
        _uiState.value = _uiState.value.copy(isExpenseAdded = false)
    }
}
