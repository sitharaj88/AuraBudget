package `in`.sitharaj.aurabudget.domain.usecase.expense

import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import javax.inject.Inject

/**
 * Use case for adding new expenses
 * Following Single Responsibility Principle
 */
class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: ExpenseEntity): Result<Long> {
        return try {
            if (!expense.isValid()) {
                Result.failure(IllegalArgumentException("Invalid expense data"))
            } else {
                val id = expenseRepository.insertExpense(expense)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
