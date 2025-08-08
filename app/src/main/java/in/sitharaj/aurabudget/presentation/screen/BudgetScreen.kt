package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.SectionHeader
import `in`.sitharaj.aurabudget.presentation.navigation.Screen

data class BudgetCategory(
    val id: Long,
    val name: String,
    val allocated: Double,
    val spent: Double,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Sample budget data
    val totalBudget = 50000.0
    val totalSpent = 32500.0
    val remaining = totalBudget - totalSpent

    val budgetCategories = listOf(
        BudgetCategory(
            id = 1,
            name = "Food & Dining",
            allocated = 12000.0,
            spent = 8500.0,
            icon = Icons.Default.Restaurant,
            color = Color(0xFF4CAF50)
        ),
        BudgetCategory(
            id = 2,
            name = "Transportation",
            allocated = 5000.0,
            spent = 4200.0,
            icon = Icons.Default.DirectionsCar,
            color = Color(0xFF2196F3)
        ),
        BudgetCategory(
            id = 3,
            name = "Shopping",
            allocated = 8000.0,
            spent = 6800.0,
            icon = Icons.Default.ShoppingBag,
            color = Color(0xFFFF9800)
        ),
        BudgetCategory(
            id = 4,
            name = "Entertainment",
            allocated = 3000.0,
            spent = 2100.0,
            icon = Icons.Default.Movie,
            color = Color(0xFF9C27B0)
        ),
        BudgetCategory(
            id = 5,
            name = "Utilities",
            allocated = 6000.0,
            spent = 5500.0,
            icon = Icons.Default.ElectricBolt,
            color = Color(0xFFFF5722)
        ),
        BudgetCategory(
            id = 6,
            name = "Healthcare",
            allocated = 4000.0,
            spent = 1200.0,
            icon = Icons.Default.LocalHospital,
            color = Color(0xFF00BCD4)
        ),
        BudgetCategory(
            id = 7,
            name = "Education",
            allocated = 5000.0,
            spent = 2800.0,
            icon = Icons.Default.School,
            color = Color(0xFF795548)
        ),
        BudgetCategory(
            id = 8,
            name = "Savings",
            allocated = 7000.0,
            spent = 1400.0,
            icon = Icons.Default.Savings,
            color = Color(0xFF607D8B)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Budget Overview") },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.AddBudget.route) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Budget")
                }
                IconButton(onClick = { /* Settings */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Budget Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "This Month's Budget",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "₹${String.format("%.0f", totalSpent)} of ₹${String.format("%.0f", totalBudget)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val progress = (totalSpent / totalBudget).coerceAtMost(1.0).toFloat()
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (progress > 0.8f) Color.Red else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Remaining",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "₹${String.format("%.0f", remaining)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Progress",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // Budget Categories Section
            item {
                SectionHeader(
                    title = "Budget Categories",
                    subtitle = "${budgetCategories.size} categories",
                    actionText = "Manage",
                    onActionClick = { navController.navigate(Screen.Categories.route) }
                )
            }

            // Budget Categories List
            items(budgetCategories) { category ->
                BudgetCategoryCard(
                    category = category,
                    onCategoryClick = {
                        navController.navigate(Screen.BudgetDetail.createRoute(category.id))
                    }
                )
            }

            // Budget Tips Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Budget Tip",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "You're doing great! Try to save 20% more on entertainment to reach your savings goal faster.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddBudget.route) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget Category")
            }
        }
    }
}

@Composable
fun BudgetCategoryCard(
    category: BudgetCategory,
    onCategoryClick: (BudgetCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (category.spent / category.allocated).coerceAtMost(1.0)
    val isOverBudget = category.spent > category.allocated

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onCategoryClick(category) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(category.color.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = category.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "₹${String.format("%.0f", category.spent)} of ₹${String.format("%.0f", category.allocated)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isOverBudget) "Over Budget" else "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isOverBudget) Color.Red else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "₹${String.format("%.0f", category.allocated - category.spent)} left",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isOverBudget) Color.Red else category.color,
                trackColor = category.color.copy(alpha = 0.2f)
            )
        }
    }
}
