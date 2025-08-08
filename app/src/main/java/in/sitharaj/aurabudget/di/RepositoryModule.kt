package `in`.sitharaj.aurabudget.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.sitharaj.aurabudget.data.repository.ExpenseRepositoryImpl
import `in`.sitharaj.aurabudget.data.repository.BudgetRepositoryImpl
import `in`.sitharaj.aurabudget.data.repository.CategoryRepositoryImpl
import `in`.sitharaj.aurabudget.data.repository.PreferencesRepositoryImpl
import `in`.sitharaj.aurabudget.domain.repository.ExpenseRepository
import `in`.sitharaj.aurabudget.domain.repository.BudgetRepository
import `in`.sitharaj.aurabudget.domain.repository.CategoryRepository
import `in`.sitharaj.aurabudget.domain.repository.PreferencesRepository
import javax.inject.Singleton

/**
 * Hilt module for repository dependencies
 * Following Dependency Inversion Principle
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository
}
