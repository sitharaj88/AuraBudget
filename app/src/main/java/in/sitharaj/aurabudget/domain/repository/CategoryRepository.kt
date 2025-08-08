package `in`.sitharaj.aurabudget.domain.repository

import kotlinx.coroutines.flow.Flow
import `in`.sitharaj.aurabudget.domain.model.CategoryEntity

/**
 * Repository interface for category management
 */
interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryEntity>>
    fun getCategoriesByType(type: CategoryEntity.CategoryType): Flow<List<CategoryEntity>>
    fun getDefaultCategories(): Flow<List<CategoryEntity>>
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(categoryId: Long)
    suspend fun toggleCategoryActive(categoryId: Long)
    suspend fun getCategoryById(categoryId: Long): CategoryEntity?
    suspend fun initializeDefaultCategories()
}
