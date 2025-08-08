package `in`.sitharaj.aurabudget.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Expense data
 * Data layer implementation separate from domain model
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val categoryId: Long,
    val date: Long,
    val description: String?,
    val tags: String = "" // Store as comma-separated string
)
