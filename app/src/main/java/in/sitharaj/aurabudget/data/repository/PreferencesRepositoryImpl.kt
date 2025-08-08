package `in`.sitharaj.aurabudget.data.repository

import `in`.sitharaj.aurabudget.data.UserPreferencesManager
import `in`.sitharaj.aurabudget.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of PreferencesRepository
 * Following Dependency Inversion Principle and Repository Pattern
 */
@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : PreferencesRepository {

    override val isDarkTheme: Flow<Boolean> = userPreferencesManager.isDarkTheme

    override suspend fun setDarkTheme(enabled: Boolean) = userPreferencesManager.setDarkTheme(enabled)
}
