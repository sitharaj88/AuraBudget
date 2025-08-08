package `in`.sitharaj.aurabudget.data.local.dao

import androidx.room.*
import `in`.sitharaj.aurabudget.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Category operations
 */
@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name ASC")
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories WHERE isDefault = 1")
    fun getDefaultCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int
}
