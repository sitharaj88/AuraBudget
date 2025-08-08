package `in`.sitharaj.aurabudget.data.repository

import `in`.sitharaj.aurabudget.data.local.dao.BudgetDao
import `in`.sitharaj.aurabudget.data.mapper.BudgetMapper
import `in`.sitharaj.aurabudget.domain.model.BudgetEntity
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of BudgetRepository
 * Following Dependency Inversion Principle and Repository Pattern
 */
@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.getAllBudgets().map { entities ->
            BudgetMapper.mapToDomainList(entities)
        }
    }

    override fun getActiveBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.getActiveBudgets().map { entities ->
            BudgetMapper.mapToDomainList(entities)
        }
    }

    override fun getBudgetsByCategory(categoryId: Long): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsByCategory(categoryId).map { entities ->
            BudgetMapper.mapToDomainList(entities)
        }
    }

    override suspend fun insertBudget(budget: BudgetEntity): Long {
        val dataEntity = BudgetMapper.mapToData(budget)
        return budgetDao.insertBudget(dataEntity)
    }

    override suspend fun updateBudget(budget: BudgetEntity) {
        val dataEntity = BudgetMapper.mapToData(budget)
        budgetDao.updateBudget(dataEntity)
    }

    override suspend fun deleteBudget(budget: BudgetEntity) {
        val dataEntity = BudgetMapper.mapToData(budget)
        budgetDao.deleteBudget(dataEntity)
    }

    override suspend fun getBudgetById(id: Long): BudgetEntity? {
        return budgetDao.getBudgetById(id)?.let { entity ->
            BudgetMapper.mapToDomain(entity)
        }
    }

    override suspend fun updateBudgetSpentAmount(budgetId: Long, spentAmount: Double) {
        budgetDao.updateBudgetSpentAmount(budgetId, spentAmount)
    }

    override fun getExpiredBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.getExpiredBudgets().map { entities ->
            BudgetMapper.mapToDomainList(entities)
        }
    }
}
