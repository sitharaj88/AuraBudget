package `in`.sitharaj.aurabudget.domain.usecase.analytics

import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.model.FinancialSummary
import `in`.sitharaj.aurabudget.domain.model.MonthlyData
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import `in`.sitharaj.aurabudget.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Use case for generating financial analytics and insights
 * Following Single Responsibility Principle
 */
class GetFinancialAnalyticsUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) {
    fun getFinancialSummary(): Flow<FinancialSummary> {
        return combine(
            expenseRepository.getAllExpenses(),
            categoryRepository.getAllCategories()
        ) { expenses, categories ->
            val categoryMap = categories.associateBy { it.id }

            val totalExpenses = expenses.sumOf { it.amount }
            val totalIncome = 0.0 // TODO: Implement income tracking

            val categoryBreakdown = expenses
                .groupBy { it.categoryId }
                .mapNotNull { (categoryId, expensesList) ->
                    categoryMap[categoryId]?.let { category ->
                        category to expensesList.sumOf { it.amount }
                    }
                }
                .toMap()

            val monthlyTrend = generateMonthlyTrend(expenses)
            val topCategories = categoryBreakdown.toList().sortedByDescending { it.second }.take(5)

            FinancialSummary(
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                netAmount = totalIncome - totalExpenses,
                categoryBreakdown = categoryBreakdown,
                monthlyTrend = monthlyTrend,
                topCategories = topCategories
            )
        }
    }

    private fun generateMonthlyTrend(expenses: List<ExpenseEntity>): List<MonthlyData> {
        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val groupedByMonth = expenses.groupBy { expense ->
            dateFormat.format(Date(expense.date))
        }

        return groupedByMonth.map { (month, monthExpenses) ->
            val totalExpenses = monthExpenses.sumOf { it.amount }
            MonthlyData(
                month = month,
                income = 0.0, // TODO: Implement income tracking
                expenses = totalExpenses,
                savings = 0.0 - totalExpenses
            )
        }.sortedBy { it.month }
    }
}
