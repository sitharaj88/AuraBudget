package `in`.sitharaj.aurabudget.domain.model

/**
 * Domain model for Category - Clean architecture entity
 * Represents expense/income categories with enhanced features
 */
data class CategoryEntity(
    val id: Long = 0,
    val name: String,
    val icon: String?,
    val color: Long?,
    val type: CategoryType = CategoryType.EXPENSE,
    val isDefault: Boolean = false,
    val isActive: Boolean = true,
    val usageCount: Int = 0,
    val monthlyBudget: Double? = null
) {
    enum class CategoryType {
        EXPENSE, INCOME, TRANSFER
    }

    fun hasValidName(): Boolean = name.isNotBlank()

    fun getDisplayName(): String = name.trim().takeIf { it.isNotEmpty() } ?: "Unnamed Category"
}
