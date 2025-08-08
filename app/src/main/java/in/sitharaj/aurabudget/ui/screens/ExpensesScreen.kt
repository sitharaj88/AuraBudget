package `in`.sitharaj.aurabudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import `in`.sitharaj.aurabudget.presentation.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by expenseViewModel.expenses.collectAsState()
    val uiState by expenseViewModel.uiState.collectAsState()
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isExpenseAdded) {
        if (uiState.isExpenseAdded) {
            showAddExpenseDialog = false
            expenseViewModel.resetExpenseAddedState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .padding(16.dp)
        ) {
            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No expenses yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to add your first expense",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenses) { expense ->
                        ExpenseItem(
                            expense = expense,
                            onDelete = { expenseViewModel.deleteExpense(expense) }
                        )
                    }
                }
            }
        }
    }

    if (showAddExpenseDialog) {
        AddExpenseDialog(
            onDismiss = { showAddExpenseDialog = false },
            onAdd = { amount, note ->
                expenseViewModel.addExpense(
                    amount = amount,
                    categoryId = 1, // Default category for now
                    description = note,
                    tags = emptyList()
                )
                showAddExpenseDialog = false
            }
        )
    }
}

@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.description ?: "No description",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(Date(expense.date)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = expense.getFormattedAmount(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAdd: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && amountValue > 0) {
                        onAdd(amountValue, note.ifBlank { null } ?: "Expense")
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
