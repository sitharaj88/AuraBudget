package `in`.sitharaj.aurabudget.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Category data
 * Data layer implementation separate from domain model
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String?,
    val color: String?,
    val type: String = "EXPENSE", // Store enum as string
    val isDefault: Boolean = false,
    val monthlyBudget: Double? = null
)
