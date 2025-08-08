package `in`.sitharaj.aurabudget.data.local.dao

import androidx.room.*
import `in`.sitharaj.aurabudget.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Budget operations
 */
@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets ORDER BY startDate DESC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE endDate > :currentDate ORDER BY startDate DESC")
    fun getActiveBudgets(currentDate: Long = System.currentTimeMillis()): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId ORDER BY startDate DESC")
    fun getBudgetsByCategory(categoryId: Long): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("UPDATE budgets SET spent = :spentAmount WHERE id = :budgetId")
    suspend fun updateBudgetSpentAmount(budgetId: Long, spentAmount: Double)

    @Query("SELECT * FROM budgets WHERE endDate < :currentDate")
    fun getExpiredBudgets(currentDate: Long = System.currentTimeMillis()): Flow<List<BudgetEntity>>
}
