package `in`.sitharaj.aurabudget.domain.usecase.expense

import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import javax.inject.Inject

/**
 * Use case for deleting expenses
 * Following Single Responsibility Principle
 */
class DeleteExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: ExpenseEntity): Result<Unit> {
        return try {
            expenseRepository.deleteExpense(expense)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
