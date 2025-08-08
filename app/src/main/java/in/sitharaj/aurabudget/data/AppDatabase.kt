package `in`.sitharaj.aurabudget.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Expense::class, Budget::class, Category::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
}
