package `in`.sitharaj.aurabudget.data.repository

import `in`.sitharaj.aurabudget.data.local.dao.CategoryDao
import `in`.sitharaj.aurabudget.data.mapper.CategoryMapper
import `in`.sitharaj.aurabudget.domain.model.CategoryEntity
import `in`.sitharaj.aurabudget.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CategoryRepository
 * Following Dependency Inversion Principle and Repository Pattern
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories().map { entities ->
            CategoryMapper.mapToDomainList(entities)
        }
    }

    override fun getCategoriesByType(type: CategoryEntity.CategoryType): Flow<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(type.name).map { entities ->
            CategoryMapper.mapToDomainList(entities)
        }
    }

    override suspend fun insertCategory(category: CategoryEntity): Long {
        val dataEntity = CategoryMapper.mapToData(category)
        return categoryDao.insertCategory(dataEntity)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        val dataEntity = CategoryMapper.mapToData(category)
        categoryDao.updateCategory(dataEntity)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        val dataEntity = CategoryMapper.mapToData(category)
        categoryDao.deleteCategory(dataEntity)
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return categoryDao.getCategoryById(id)?.let { entity ->
            CategoryMapper.mapToDomain(entity)
        }
    }

    override fun getDefaultCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getDefaultCategories().map { entities ->
            CategoryMapper.mapToDomainList(entities)
        }
    }

    override suspend fun initializeDefaultCategories() {
        val count = categoryDao.getCategoryCount()
        if (count == 0) {
            val defaultCategories = listOf(
                CategoryEntity(name = "Food & Dining", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "food_icon", color = "red"),
                CategoryEntity(name = "Transportation", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "transport_icon", color = "blue"),
                CategoryEntity(name = "Shopping", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "shopping_icon", color = "green"),
                CategoryEntity(name = "Entertainment", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "entertainment_icon", color = "yellow"),
                CategoryEntity(name = "Bills & Utilities", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "bills_icon", color = "purple"),
                CategoryEntity(name = "Healthcare", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "healthcare_icon", color = "orange"),
                CategoryEntity(name = "Travel", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "travel_icon", color = "cyan"),
                CategoryEntity(name = "Other", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "other_icon", color = "magenta")
            )

            defaultCategories.forEach { category ->
                insertCategory(category)
            }
        }
    }
}
