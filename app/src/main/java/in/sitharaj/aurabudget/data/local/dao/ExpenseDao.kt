package `in`.sitharaj.aurabudget.data.local.dao

import androidx.room.*
import `in`.sitharaj.aurabudget.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class for category totals
 */
data class CategoryTotal(
    val categoryId: Long,
    val total: Double
)

/**
 * Enhanced DAO for Expense operations with advanced queries
 * Following Interface Segregation Principle
 */
@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalExpensesForPeriod(startDate: Long, endDate: Long): Flow<Double>

    @Query("SELECT categoryId, SUM(amount) as total FROM expenses GROUP BY categoryId")
    fun getExpensesByCategoryTotal(): Flow<List<CategoryTotal>>

    @Query("SELECT * FROM expenses WHERE description LIKE '%' || :searchQuery || '%' ORDER BY date DESC")
    fun searchExpenses(searchQuery: String): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpenseCount(): Int
}
