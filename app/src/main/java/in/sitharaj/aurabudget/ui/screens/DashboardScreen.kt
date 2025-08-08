package `in`.sitharaj.aurabudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.viewmodel.ExpenseViewModel
import `in`.sitharaj.aurabudget.presentation.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    budgetViewModel: BudgetViewModel = hiltViewModel()
) {
    val expenses by expenseViewModel.expenses.collectAsState()
    val budgets by budgetViewModel.budgets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Open drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("expenses") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Total Balance Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Total Expenses",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "$${expenses.sumOf { it.amount }}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Recent Expenses",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (expenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No expenses yet. Tap + to add your first expense!",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                items(expenses.take(5)) { expense ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = expense.description ?: "No description",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Category ID: ${expense.categoryId}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = expense.getFormattedAmount(),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
