package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.*
import `in`.sitharaj.aurabudget.presentation.navigation.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }

    // Sample transactions data
    val allTransactions = listOf(
        Transaction(
            id = 1,
            amount = 2500.0,
            category = "Groceries",
            description = "Weekly shopping at supermarket",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.ShoppingCart,
            date = LocalDateTime.now().minusDays(1)
        ),
        Transaction(
            id = 2,
            amount = 50000.0,
            category = "Salary",
            description = "Monthly salary credit",
            type = TransactionType.INCOME,
            categoryIcon = Icons.Default.Work,
            date = LocalDateTime.now().minusDays(2)
        ),
        Transaction(
            id = 3,
            amount = 1200.0,
            category = "Transportation",
            description = "Fuel for car",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.DirectionsCar,
            date = LocalDateTime.now().minusDays(3)
        ),
        Transaction(
            id = 4,
            amount = 800.0,
            category = "Entertainment",
            description = "Movie tickets for family",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.Movie,
            date = LocalDateTime.now().minusDays(4)
        ),
        Transaction(
            id = 5,
            amount = 5000.0,
            category = "Freelance",
            description = "Website development project",
            type = TransactionType.INCOME,
            categoryIcon = Icons.Default.Computer,
            date = LocalDateTime.now().minusDays(5)
        ),
        Transaction(
            id = 6,
            amount = 1500.0,
            category = "Utilities",
            description = "Electricity bill",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.ElectricBolt,
            date = LocalDateTime.now().minusDays(6)
        )
    )

    val filteredTransactions = when (selectedFilter) {
        "Income" -> allTransactions.filter { it.type == TransactionType.INCOME }
        "Expenses" -> allTransactions.filter { it.type == TransactionType.EXPENSE }
        else -> allTransactions
    }

    val totalIncome = allTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpenses = allTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Transactions") },
            actions = {
                IconButton(onClick = { /* Search functionality */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* Filter options */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Green.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color.Green
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Income",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "₹${String.format("%.0f", totalIncome)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Expenses",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "₹${String.format("%.0f", totalExpenses)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Income", "Expenses").forEach { filter ->
                        FilterChip(
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            selected = selectedFilter == filter
                        )
                    }
                }
            }

            // Transactions List
            items(filteredTransactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onTransactionClick = {
                        navController.navigate(Screen.TransactionDetail.createRoute(transaction.id))
                    }
                )
            }

            // Add Transaction FAB space
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button for adding transaction
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddTransaction.route) },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    }
}
