package `in`.sitharaj.aurabudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val categoryId: Long,
    val date: Long,
    val note: String? = null
)
