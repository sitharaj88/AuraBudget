package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import `in`.sitharaj.aurabudget.domain.model.FinancialGoal
import `in`.sitharaj.aurabudget.domain.repository.GoalsRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for Goals Screen - Manages financial goals and savings tracking
 */
@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            goalsRepository.getAllGoals().collect { goals ->
                _uiState.value = _uiState.value.copy(
                    goals = goals,
                    isLoading = false
                )
            }
        }
    }

    fun addGoal(name: String, targetAmount: Double, description: String) {
        viewModelScope.launch {
            try {
                val newGoal = FinancialGoal(
                    id = 0, // ID will be auto-generated
                    name = name,
                    targetAmount = targetAmount,
                    currentAmount = 0.0, // Default to 0
                    targetDate = LocalDate.now().plusYears(1), // Default 1 year target
                    categoryId = null, // No category by default
                    description = description.ifBlank { null }
                )
                goalsRepository.insertGoal(newGoal)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun editGoal(goal: FinancialGoal) {
        _uiState.value = _uiState.value.copy(selectedGoal = goal)
    }

    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            try {
                goalsRepository.deleteGoal(goalId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun showAddMoneyDialog(goal: FinancialGoal) {
        _uiState.value = _uiState.value.copy(
            selectedGoal = goal,
            showAddMoneyDialog = true
        )
    }

    fun addMoneyToGoal(goalId: Long, amount: Double) {
        viewModelScope.launch {
            try {
                goalsRepository.addMoneyToGoal(goalId, amount)
                _uiState.value = _uiState.value.copy(showAddMoneyDialog = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(
            showAddMoneyDialog = false,
            selectedGoal = null
        )
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
}

data class GoalsUiState(
    val goals: List<FinancialGoal> = emptyList(),
    val selectedGoal: FinancialGoal? = null,
    val showAddMoneyDialog: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)
