package `in`.sitharaj.aurabudget.domain.model

import java.time.LocalDate

/**
 * Enhanced financial goal tracking model
 */
data class FinancialGoal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val targetDate: LocalDate,
    val categoryId: Long?,
    val description: String?,
    val isCompleted: Boolean = false
) {
    fun getProgress(): Float = if (targetAmount > 0) (currentAmount / targetAmount).toFloat() else 0f

    fun getRemainingAmount(): Double = targetAmount - currentAmount

    fun getDaysRemaining(): Int {
        // Calculate days until target date
        return 30 // Placeholder
    }

    fun isOnTrack(): Boolean {
        val progressPercentage = getProgress()
        val timeProgressPercentage = 0.5f // Placeholder calculation
        return progressPercentage >= timeProgressPercentage
    }
}

/**
 * Transaction model for comprehensive tracking
 */
data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val accountId: Long,
    val date: Long,
    val description: String?,
    val tags: List<String> = emptyList(),
    val location: String? = null,
    val receipt: String? = null // Image path
) {
    enum class TransactionType { INCOME, EXPENSE, TRANSFER }

    fun getFormattedAmount(): String = String.format("$%.2f", amount)
}

/**
 * Account model for multiple financial accounts
 */
data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val currency: String = "USD",
    val isDefault: Boolean = false
) {
    enum class AccountType { CASH, BANK, CREDIT_CARD, SAVINGS, INVESTMENT }

    fun getFormattedBalance(): String = String.format("$%.2f", balance)
}
