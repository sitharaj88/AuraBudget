package `in`.sitharaj.aurabudget.domain.model

/**
 * Domain model for Financial Insights and Analytics
 */
data class FinancialSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netAmount: Double,
    val categoryBreakdown: Map<CategoryEntity, Double>,
    val monthlyTrend: List<MonthlyData>,
    val topCategories: List<Pair<CategoryEntity, Double>>
) {
    fun getSavingsRate(): Double {
        return if (totalIncome > 0) ((totalIncome - totalExpenses) / totalIncome) * 100 else 0.0
    }

    fun isHealthyFinancially(): Boolean = netAmount > 0 && getSavingsRate() >= 20.0
}

data class MonthlyData(
    val month: String,
    val income: Double,
    val expenses: Double,
    val savings: Double
)
