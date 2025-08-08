package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.ExpenseCard
import `in`.sitharaj.aurabudget.presentation.viewmodel.ExpenseViewModel

/**
 * Enhanced Expense Management Screen with search and filtering
 * Following MVVM pattern and clean architecture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedExpenseScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter expenses based on search query
    val filteredExpenses = remember(expenses, searchQuery) {
        if (searchQuery.isEmpty()) {
            expenses
        } else {
            expenses.filter { expense ->
                expense.description?.contains(searchQuery, ignoreCase = true) == true ||
                expense.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    LaunchedEffect(uiState.isExpenseAdded) {
        if (uiState.isExpenseAdded) {
            showAddExpenseDialog = false
            viewModel.resetExpenseAddedState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Expenses",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement filter */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExpenseDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchExpenses(it)
                },
                label = { Text("Search expenses...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Expense Summary
            if (filteredExpenses.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Total Expenses",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = String.format("$%.2f", filteredExpenses.sumOf { it.amount }),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Count",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${filteredExpenses.size}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Expenses List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredExpenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty()) "No expenses yet" else "No matching expenses",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isEmpty()) "Tap + to add your first expense" else "Try adjusting your search",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredExpenses) { expense ->
                        ExpenseCard(
                            expense = expense,
                            onEdit = { /* TODO: Implement edit */ },
                            onDelete = { /* TODO: Implement delete */ }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Add Expense Dialog
    if (showAddExpenseDialog) {
        AddExpenseDialog(
            onDismiss = { showAddExpenseDialog = false },
            onAdd = { amount, description, tags ->
                viewModel.addExpense(
                    amount = amount,
                    categoryId = 1, // TODO: Category selection
                    description = description,
                    tags = tags
                )
            },
            isLoading = uiState.isLoading,
            error = uiState.error
        )
    }

    // Error handling
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or handle error
            viewModel.clearError()
        }
    }
}

@Composable
private fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAdd: (Double, String, List<String>) -> Unit,
    isLoading: Boolean = false,
    error: String? = null
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add New Expense",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("food, groceries, shopping") }
                )

                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && amountValue > 0 && description.isNotBlank()) {
                        val tagsList = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onAdd(amountValue, description, tagsList)
                    }
                },
                enabled = !isLoading && amount.toDoubleOrNull() != null && description.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancel")
            }
        }
    )
}
