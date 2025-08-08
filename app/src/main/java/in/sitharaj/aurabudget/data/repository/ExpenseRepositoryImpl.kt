package `in`.sitharaj.aurabudget.data.repository

import `in`.sitharaj.aurabudget.data.local.dao.ExpenseDao
import `in`.sitharaj.aurabudget.data.mapper.ExpenseMapper
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ExpenseRepository
 * Following Dependency Inversion Principle and Repository Pattern
 */
@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpenses().map { entities ->
            ExpenseMapper.mapToDomainList(entities)
        }
    }

    override fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesByCategory(categoryId).map { entities ->
            ExpenseMapper.mapToDomainList(entities)
        }
    }

    override fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate).map { entities ->
            ExpenseMapper.mapToDomainList(entities)
        }
    }

    override suspend fun insertExpense(expense: ExpenseEntity): Long {
        val dataEntity = ExpenseMapper.mapToData(expense)
        return expenseDao.insertExpense(dataEntity)
    }

    override suspend fun updateExpense(expense: ExpenseEntity) {
        val dataEntity = ExpenseMapper.mapToData(expense)
        expenseDao.updateExpense(dataEntity)
    }

    override suspend fun deleteExpense(expense: ExpenseEntity) {
        val dataEntity = ExpenseMapper.mapToData(expense)
        expenseDao.deleteExpense(dataEntity)
    }

    override suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenseDao.getExpenseById(id)?.let { entity ->
            ExpenseMapper.mapToDomain(entity)
        }
    }

    override fun getTotalExpensesForMonth(month: Int, year: Int): Flow<Double> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startDate = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endDate = calendar.timeInMillis

        return expenseDao.getTotalExpensesForPeriod(startDate, endDate)
    }

    override fun getExpensesGroupedByCategory(): Flow<Map<Long, List<ExpenseEntity>>> {
        return expenseDao.getAllExpenses().map { entities ->
            ExpenseMapper.mapToDomainList(entities).groupBy { it.categoryId }
        }
    }

    fun getExpensesByCategoryTotal(): Flow<Map<Long, Double>> {
        return expenseDao.getExpensesByCategoryTotal().map { categoryTotals ->
            categoryTotals.associate { it.categoryId to it.total }
        }
    }
}
