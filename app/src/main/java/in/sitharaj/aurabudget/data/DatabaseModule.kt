package `in`.sitharaj.aurabudget.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.sitharaj.aurabudget.data.local.dao.ExpenseDao
import `in`.sitharaj.aurabudget.data.local.dao.BudgetDao
import `in`.sitharaj.aurabudget.data.local.dao.CategoryDao
import `in`.sitharaj.aurabudget.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "aurabudget_db").build()

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()
}
