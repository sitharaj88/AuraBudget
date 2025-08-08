package `in`.sitharaj.aurabudget.domain.model

/**
 * Domain model for Budget - Clean architecture entity
 * Represents budget planning and tracking
 */
data class BudgetEntity(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val spent: Double = 0.0,
    val categoryId: Long?,
    val startDate: Long,
    val endDate: Long,
    val isRecurring: Boolean = false
) {
    fun getRemainingAmount(): Double = amount - spent

    fun getSpentPercentage(): Float = if (amount > 0) (spent / amount).toFloat() else 0f

    fun isOverBudget(): Boolean = spent > amount

    fun isExpired(): Boolean = System.currentTimeMillis() > endDate

    fun getDaysRemaining(): Long {
        val currentTime = System.currentTimeMillis()
        return if (currentTime < endDate) {
            (endDate - currentTime) / (24 * 60 * 60 * 1000)
        } else 0
    }
}
