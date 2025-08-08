package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.charts.*
import `in`.sitharaj.aurabudget.presentation.components.*
import `in`.sitharaj.aurabudget.presentation.viewmodel.AnalyticsViewModel
import `in`.sitharaj.aurabudget.domain.model.FinancialGoal
import java.util.Locale

/**
 * Advanced Analytics Dashboard with comprehensive financial insights
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Analytics",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.exportData() }) {
                        Icon(Icons.Default.Download, contentDescription = "Export")
                    }
                    IconButton(onClick = { /* TODO: Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Financial Overview Cards
            item {
                Text(
                    text = "Financial Overview",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        FinancialMetricCard(
                            title = "Total Income",
                            amount = uiState.totalIncome,
                            icon = Icons.Default.TrendingUp,
                            color = Color(0xFF4CAF50),
                            change = "+12.5%"
                        )
                    }
                    item {
                        FinancialMetricCard(
                            title = "Total Expenses",
                            amount = uiState.totalExpenses,
                            icon = Icons.Default.TrendingDown,
                            color = Color(0xFFF44336),
                            change = "+5.2%"
                        )
                    }
                    item {
                        FinancialMetricCard(
                            title = "Net Savings",
                            amount = uiState.netSavings,
                            icon = Icons.Default.AccountBalance,
                            color = Color(0xFF2196F3),
                            change = "+25.8%"
                        )
                    }
                    item {
                        FinancialMetricCard(
                            title = "Savings Rate",
                            amount = uiState.savingsRate,
                            icon = Icons.Default.Percent,
                            color = Color(0xFF9C27B0),
                            change = "+3.2%",
                            isPercentage = true
                        )
                    }
                }
            }
            
            // Spending by Category Chart
            item {
                ExpensePieChart(
                    data = uiState.expensesByCategory,
                    colors = listOf(
                        Color(0xFF6200EE),
                        Color(0xFF03DAC6),
                        Color(0xFFFF6B6B),
                        Color(0xFF4ECDC4),
                        Color(0xFF45B7D1),
                        Color(0xFF96CEB4),
                        Color(0xFFFD79A8),
                        Color(0xFFE84393)
                    )
                )
            }
            
            // Monthly Trend Chart
            item {
                SpendingTrendChart(
                    data = uiState.monthlyTrend
                )
            }
            
            // Budget Progress Section
            item {
                Text(
                    text = "Budget Progress",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            items(uiState.budgetProgress) { budget ->
                BudgetProgressChart(
                    budgetName = budget.name,
                    spent = budget.spent,
                    budget = budget.amount
                )
            }
            
            // Goals Section
            item {
                Text(
                    text = "Financial Goals",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            items(uiState.financialGoals) { goal ->
                GoalProgressCard(
                    goal = goal,
                    onGoalClick = { /* TODO: Navigate to goal details */ }
                )
            }
            
            // Insights & Recommendations
            item {
                InsightsCard(insights = uiState.insights)
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun FinancialMetricCard(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    change: String,
    isPercentage: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = change,
                    fontSize = 12.sp,
                    color = if (change.startsWith("+")) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Text(
                text = if (isPercentage) "${amount.toInt()}%" else String.format(Locale.US, "$%.2f", amount),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun GoalProgressCard(
    goal: FinancialGoal,
    onGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onGoalClick
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "${String.format(Locale.US, "$%.0f", goal.currentAmount)} of ${String.format(Locale.US, "$%.0f", goal.targetAmount)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Text(
                    text = "${(goal.getProgress() * 100).toInt()}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { goal.getProgress() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Remaining: ${String.format(Locale.US, "$%.0f", goal.getRemainingAmount())}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Text(
                    text = if (goal.isOnTrack()) "✅ On track" else "⚠️ Behind",
                    fontSize = 12.sp,
                    color = if (goal.isOnTrack()) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }
        }
    }
}

@Composable
fun InsightsCard(
    insights: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Insights",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Smart Insights",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            insights.forEach { insight ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "💡",
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = insight,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
