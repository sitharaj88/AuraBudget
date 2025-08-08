package `in`.sitharaj.aurabudget.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.sitharaj.aurabudget.data.local.dao.ExpenseDao
import `in`.sitharaj.aurabudget.data.local.dao.BudgetDao
import `in`.sitharaj.aurabudget.data.local.dao.CategoryDao
import `in`.sitharaj.aurabudget.data.local.entity.ExpenseEntity
import `in`.sitharaj.aurabudget.data.local.entity.BudgetEntity
import `in`.sitharaj.aurabudget.data.local.entity.CategoryEntity

/**
 * Room database for AuraBudget app
 * Following clean architecture principles
 */
@Database(
    entities = [ExpenseEntity::class, BudgetEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
}
