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

    override suspend fun insertCategory(category: CategoryEntity) {
        val dataEntity = CategoryMapper.mapToData(category)
        categoryDao.insertCategory(dataEntity)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        val dataEntity = CategoryMapper.mapToData(category)
        categoryDao.updateCategory(dataEntity)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        // Get the category first, then delete it
        val category = categoryDao.getCategoryById(categoryId)
        category?.let { categoryDao.deleteCategory(it) }
    }

    override suspend fun toggleCategoryActive(categoryId: Long) {
        // Get the category, toggle isActive, then update
        val category = categoryDao.getCategoryById(categoryId)
        category?.let {
            val updatedCategory = it.copy(isActive = !it.isActive)
            categoryDao.updateCategory(updatedCategory)
        }
    }

    override suspend fun getCategoryById(categoryId: Long): CategoryEntity? {
        return categoryDao.getCategoryById(categoryId)?.let { entity ->
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
                CategoryEntity(name = "Food & Dining", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "food_icon", color = 0xFF6200EE),
                CategoryEntity(name = "Transportation", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "transport_icon", color = 0xFF03DAC6),
                CategoryEntity(name = "Shopping", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "shopping_icon", color = 0xFFFF6B6B),
                CategoryEntity(name = "Entertainment", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "entertainment_icon", color = 0xFF4ECDC4),
                CategoryEntity(name = "Bills & Utilities", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "bills_icon", color = 0xFF45B7D1),
                CategoryEntity(name = "Healthcare", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "healthcare_icon", color = 0xFF96CEB4),
                CategoryEntity(name = "Travel", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "travel_icon", color = 0xFFFD79A8),
                CategoryEntity(name = "Education", type = CategoryEntity.CategoryType.EXPENSE, isDefault = true, icon = "education_icon", color = 0xFFE84393)
            )

            defaultCategories.forEach { category ->
                insertCategory(category)
            }
        }
    }
}
