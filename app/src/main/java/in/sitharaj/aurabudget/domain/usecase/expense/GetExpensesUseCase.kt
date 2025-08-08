package `in`.sitharaj.aurabudget.domain.usecase.expense

import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting expenses with filtering and sorting
 * Following Single Responsibility Principle
 */
class GetExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<ExpenseEntity>> {
        return expenseRepository.getAllExpenses()
    }

    fun getByCategory(categoryId: Long): Flow<List<ExpenseEntity>> {
        return expenseRepository.getExpensesByCategory(categoryId)
    }

    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> {
        return expenseRepository.getExpensesByDateRange(startDate, endDate)
    }
}
