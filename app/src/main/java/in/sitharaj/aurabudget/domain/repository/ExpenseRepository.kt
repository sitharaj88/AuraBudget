package `in`.sitharaj.aurabudget.domain.repository

import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Expense operations
 * Following Repository pattern and Dependency Inversion Principle
 */
interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>>
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>
    suspend fun insertExpense(expense: ExpenseEntity): Long
    suspend fun updateExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    fun getTotalExpensesForMonth(month: Int, year: Int): Flow<Double>
    fun getExpensesGroupedByCategory(): Flow<Map<Long, List<ExpenseEntity>>>
}
