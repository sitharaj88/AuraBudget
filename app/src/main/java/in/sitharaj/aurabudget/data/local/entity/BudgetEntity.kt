package `in`.sitharaj.aurabudget.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Budget data
 * Data layer implementation separate from domain model
 */
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val spent: Double = 0.0,
    val categoryId: Long?,
    val startDate: Long,
    val endDate: Long,
    val isRecurring: Boolean = false
)
