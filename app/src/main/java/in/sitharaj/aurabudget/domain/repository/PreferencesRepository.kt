package `in`.sitharaj.aurabudget.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user preferences operations
 * Following Repository pattern and Dependency Inversion Principle
 */
interface PreferencesRepository {
    val isDarkTheme: Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
}
