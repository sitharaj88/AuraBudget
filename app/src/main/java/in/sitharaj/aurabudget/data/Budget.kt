package `in`.sitharaj.aurabudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    val startDate: Long,
    val endDate: Long
)
