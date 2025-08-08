package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.TransactionType

data class CategoryIcon(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    transactionType: String = "expense",
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(TransactionType.valueOf(transactionType.uppercase())) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoryIcon?>(null) }
    var selectedDate by remember { mutableStateOf("Today") }

    val expenseCategories = listOf(
        CategoryIcon("Food", Icons.Default.Restaurant, Color(0xFF4CAF50)),
        CategoryIcon("Transport", Icons.Default.DirectionsCar, Color(0xFF2196F3)),
        CategoryIcon("Shopping", Icons.Default.ShoppingBag, Color(0xFFFF9800)),
        CategoryIcon("Entertainment", Icons.Default.Movie, Color(0xFF9C27B0)),
        CategoryIcon("Bills", Icons.Default.Receipt, Color(0xFFFF5722)),
        CategoryIcon("Health", Icons.Default.LocalHospital, Color(0xFF00BCD4)),
        CategoryIcon("Education", Icons.Default.School, Color(0xFF795548)),
        CategoryIcon("Travel", Icons.Default.Flight, Color(0xFF607D8B)),
        CategoryIcon("Gifts", Icons.Default.CardGiftcard, Color(0xFFE91E63)),
        CategoryIcon("Others", Icons.Default.MoreHoriz, Color(0xFF9E9E9E))
    )

    val incomeCategories = listOf(
        CategoryIcon("Salary", Icons.Default.Work, Color(0xFF4CAF50)),
        CategoryIcon("Freelance", Icons.Default.Computer, Color(0xFF2196F3)),
        CategoryIcon("Investment", Icons.Default.TrendingUp, Color(0xFFFF9800)),
        CategoryIcon("Business", Icons.Default.Business, Color(0xFF9C27B0)),
        CategoryIcon("Rental", Icons.Default.Home, Color(0xFFFF5722)),
        CategoryIcon("Bonus", Icons.Default.Star, Color(0xFF00BCD4)),
        CategoryIcon("Gift", Icons.Default.CardGiftcard, Color(0xFFE91E63)),
        CategoryIcon("Others", Icons.Default.MoreHoriz, Color(0xFF9E9E9E))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Add Transaction") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        // Save transaction logic
                        navController.navigateUp()
                    },
                    enabled = amount.isNotEmpty() && selectedCategory != null
                ) {
                    Text("Save")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Transaction Type Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(4.dp)
                ) {
                    TransactionType.values().forEach { type ->
                        val isSelected = selectedType == type
                        val color = when (type) {
                            TransactionType.INCOME -> Color.Green
                            TransactionType.EXPENSE -> Color.Red
                            TransactionType.TRANSFER -> Color.Blue
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedType = type },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) color.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = when (type) {
                                        TransactionType.INCOME -> Icons.Default.TrendingUp
                                        TransactionType.EXPENSE -> Icons.Default.TrendingDown
                                        TransactionType.TRANSFER -> Icons.Default.SwapHoriz
                                    },
                                    contentDescription = type.name,
                                    tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Amount Input
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Enter amount") },
                        prefix = { Text("₹") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Category Selection
            Column {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    val categories = if (selectedType == TransactionType.INCOME) incomeCategories else expenseCategories
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
            }

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                placeholder = { Text("Add a note...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Date Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Show date picker */ }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedDate,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick Amount Buttons
            if (selectedType == TransactionType.EXPENSE) {
                Column {
                    Text(
                        text = "Quick Amounts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("₹100", "₹500", "₹1000", "₹2000", "₹5000")) { quickAmount ->
                            FilterChip(
                                onClick = { amount = quickAmount.removePrefix("₹") },
                                label = { Text(quickAmount) },
                                selected = false
                            )
                        }
                    }
                }
            }

            // Save Button
            Button(
                onClick = {
                    // Save transaction logic
                    navController.navigateUp()
                },
                enabled = amount.isNotEmpty() && selectedCategory != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Transaction",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryIcon,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                category.color.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, category.color)
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (isSelected) category.color else category.color.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = if (isSelected) Color.White else category.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) category.color else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
