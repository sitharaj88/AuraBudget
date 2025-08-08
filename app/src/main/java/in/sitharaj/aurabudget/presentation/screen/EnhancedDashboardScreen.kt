package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
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
import `in`.sitharaj.aurabudget.presentation.components.FinancialSummaryCard
import `in`.sitharaj.aurabudget.presentation.components.CategorySpendingItem
import `in`.sitharaj.aurabudget.presentation.components.ExpenseCard
import `in`.sitharaj.aurabudget.presentation.viewmodel.DashboardViewModel

/**
 * Enhanced Dashboard Screen with financial analytics and insights
 * Following MVVM pattern and clean architecture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedDashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "AuraBudget",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("expenses") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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

                // Financial Summary Cards
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        item {
                            FinancialSummaryCard(
                                title = "Total Expenses",
                                amount = uiState.totalExpenses,
                                subtitle = "This month",
                                trend = "↑ 12%",
                                isPositive = false,
                                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.width(200.dp)
                            )
                        }

                        item {
                            FinancialSummaryCard(
                                title = "Savings Rate",
                                amount = uiState.savingsRate,
                                subtitle = "Target: 20%",
                                trend = if (uiState.savingsRate >= 20) "✓ On track" else "⚠ Below target",
                                isPositive = uiState.savingsRate >= 20,
                                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.width(200.dp)
                            )
                        }

                        item {
                            FinancialSummaryCard(
                                title = "Monthly Budget",
                                amount = uiState.monthlyBudget,
                                subtitle = "Remaining: $${uiState.monthlyBudget - uiState.totalExpenses}",
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                }

                // Top Categories Section
                if (uiState.topCategories.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Top Spending Categories",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                uiState.topCategories.forEachIndexed { index, (categoryName, amount) ->
                                    val percentage = if (uiState.totalExpenses > 0) {
                                        (amount / uiState.totalExpenses * 100).toFloat()
                                    } else 0f

                                    CategorySpendingItem(
                                        categoryName = categoryName,
                                        amount = amount,
                                        percentage = percentage,
                                        color = getCategoryColor(index)
                                    )

                                    if (index < uiState.topCategories.size - 1) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Recent Expenses Section
                if (uiState.recentExpenses.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Expenses",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(uiState.recentExpenses) { expense ->
                        ExpenseCard(
                            expense = expense,
                            onEdit = { navController.navigate("expenses") },
                            onDelete = { /* TODO: Implement delete */ }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
private fun getCategoryColor(index: Int): Color {
    val colors = listOf(
        Color(0xFF6200EE),
        Color(0xFF03DAC6),
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFF96CEB4),
        Color(0xFFFD79A8),
        Color(0xFFE84393)
    )
    return colors[index % colors.size]
}
