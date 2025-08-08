package `in`.sitharaj.aurabudget.domain.repository

import kotlinx.coroutines.flow.Flow
import `in`.sitharaj.aurabudget.domain.model.FinancialGoal

/**
 * Repository interface for financial goals
 */
interface GoalsRepository {
    fun getAllGoals(): Flow<List<FinancialGoal>>
    suspend fun insertGoal(goal: FinancialGoal)
    suspend fun updateGoal(goal: FinancialGoal)
    suspend fun deleteGoal(goalId: Long)
    suspend fun addMoneyToGoal(goalId: Long, amount: Double)
    suspend fun getGoalById(goalId: Long): FinancialGoal?
}
