package `in`.sitharaj.aurabudget.domain.repository

import `in`.sitharaj.aurabudget.domain.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Category operations
 * Following Repository pattern and Dependency Inversion Principle
 */
interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryEntity>>
    fun getCategoriesByType(type: CategoryEntity.CategoryType): Flow<List<CategoryEntity>>
    suspend fun insertCategory(category: CategoryEntity): Long
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)
    suspend fun getCategoryById(id: Long): CategoryEntity?
    fun getDefaultCategories(): Flow<List<CategoryEntity>>
    suspend fun initializeDefaultCategories()
}
