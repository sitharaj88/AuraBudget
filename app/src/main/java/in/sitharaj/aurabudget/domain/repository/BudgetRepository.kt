package `in`.sitharaj.aurabudget.domain.repository

import `in`.sitharaj.aurabudget.domain.model.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Budget operations
 * Following Repository pattern and Dependency Inversion Principle
 */
interface BudgetRepository {
    fun getAllBudgets(): Flow<List<BudgetEntity>>
    fun getActiveBudgets(): Flow<List<BudgetEntity>>
    fun getBudgetsByCategory(categoryId: Long): Flow<List<BudgetEntity>>
    suspend fun insertBudget(budget: BudgetEntity): Long
    suspend fun updateBudget(budget: BudgetEntity)
    suspend fun deleteBudget(budget: BudgetEntity)
    suspend fun getBudgetById(id: Long): BudgetEntity?
    suspend fun updateBudgetSpentAmount(budgetId: Long, spentAmount: Double)
    fun getExpiredBudgets(): Flow<List<BudgetEntity>>
}
