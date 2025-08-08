package `in`.sitharaj.aurabudget.domain.model

/**
 * Domain model for Expense - Clean architecture entity
 * Represents the core business entity without any framework dependencies
 */
data class ExpenseEntity(
    val id: Long = 0,
    val amount: Double,
    val categoryId: Long,
    val date: Long,
    val description: String?,
    val tags: List<String> = emptyList()
) {
    fun isValid(): Boolean {
        return amount > 0 && description?.isNotBlank() == true
    }

    fun getFormattedAmount(): String {
        return String.format("$%.2f", amount)
    }
}
