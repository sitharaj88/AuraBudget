package `in`.sitharaj.aurabudget.data.repository

import `in`.sitharaj.aurabudget.domain.model.FinancialGoal
import `in`.sitharaj.aurabudget.domain.repository.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GoalsRepository
 * This is a simple in-memory implementation for now
 * TODO: Add database persistence using Room
 */
@Singleton
class GoalsRepositoryImpl @Inject constructor() : GoalsRepository {

    private val goals = mutableListOf<FinancialGoal>()

    override fun getAllGoals(): Flow<List<FinancialGoal>> = flow {
        // Return default goals if empty
        if (goals.isEmpty()) {
            goals.addAll(getDefaultGoals())
        }
        emit(goals.toList())
    }

    override suspend fun insertGoal(goal: FinancialGoal) {
        val newGoal = goal.copy(id = generateId())
        goals.add(newGoal)
    }

    override suspend fun updateGoal(goal: FinancialGoal) {
        val index = goals.indexOfFirst { it.id == goal.id }
        if (index != -1) {
            goals[index] = goal
        }
    }

    override suspend fun deleteGoal(goalId: Long) {
        goals.removeAll { it.id == goalId }
    }

    override suspend fun addMoneyToGoal(goalId: Long, amount: Double) {
        val index = goals.indexOfFirst { it.id == goalId }
        if (index != -1) {
            val goal = goals[index]
            val updatedGoal = goal.copy(
                currentAmount = goal.currentAmount + amount,
                isCompleted = (goal.currentAmount + amount) >= goal.targetAmount
            )
            goals[index] = updatedGoal
        }
    }

    override suspend fun getGoalById(goalId: Long): FinancialGoal? {
        return goals.find { it.id == goalId }
    }

    private fun generateId(): Long {
        return System.currentTimeMillis()
    }

    private fun getDefaultGoals(): List<FinancialGoal> {
        return listOf(
            FinancialGoal(
                id = 1,
                name = "Emergency Fund",
                targetAmount = 10000.0,
                currentAmount = 2500.0,
                targetDate = LocalDate.now().plusMonths(12),
                categoryId = null,
                description = "Build an emergency fund for unexpected expenses"
            ),
            FinancialGoal(
                id = 2,
                name = "Vacation Fund",
                targetAmount = 5000.0,
                currentAmount = 1200.0,
                targetDate = LocalDate.now().plusMonths(8),
                categoryId = null,
                description = "Save for a dream vacation"
            ),
            FinancialGoal(
                id = 3,
                name = "New Car",
                targetAmount = 25000.0,
                currentAmount = 8000.0,
                targetDate = LocalDate.now().plusMonths(18),
                categoryId = null,
                description = "Save for a reliable car"
            )
        )
    }
}
