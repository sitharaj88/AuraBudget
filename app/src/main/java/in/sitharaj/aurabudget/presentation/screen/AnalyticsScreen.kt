package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.SectionHeader

data class AnalyticsCard(
    val title: String,
    val value: String,
    val change: String,
    val isPositive: Boolean,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf("This Month") }

    val analyticsCards = listOf(
        AnalyticsCard(
            title = "Total Income",
            value = "₹75,000",
            change = "+12%",
            isPositive = true,
            icon = Icons.Default.TrendingUp,
            color = Color.Green
        ),
        AnalyticsCard(
            title = "Total Expenses",
            value = "₹32,500",
            change = "-8%",
            isPositive = true,
            icon = Icons.Default.TrendingDown,
            color = Color.Red
        ),
        AnalyticsCard(
            title = "Savings Rate",
            value = "57%",
            change = "+5%",
            isPositive = true,
            icon = Icons.Default.Savings,
            color = Color.Blue
        ),
        AnalyticsCard(
            title = "Budget Adherence",
            value = "85%",
            change = "+3%",
            isPositive = true,
            icon = Icons.Default.CheckCircle,
            color = Color.Red
        )
    )

    val categoryBreakdown = listOf(
        Triple("Food & Dining", 26.1, Color(0xFF4CAF50)),
        Triple("Transportation", 12.9, Color(0xFF2196F3)),
        Triple("Shopping", 20.9, Color(0xFFFF9800)),
        Triple("Entertainment", 6.5, Color(0xFF9C27B0)),
        Triple("Utilities", 16.9, Color(0xFFFF5722)),
        Triple("Others", 16.7, Color(0xFF607D8B))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Analytics") },
            actions = {
                IconButton(onClick = { /* Export data */ }) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Export")
                }
                IconButton(onClick = { /* Share insights */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Period Selection
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("This Week", "This Month", "This Year", "All Time")) { period ->
                        FilterChip(
                            onClick = { selectedPeriod = period },
                            label = { Text(period) },
                            selected = selectedPeriod == period
                        )
                    }
                }
            }

            // Key Metrics Cards
            item {
                Column {
                    SectionHeader(
                        title = "Key Metrics",
                        subtitle = selectedPeriod.lowercase()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(analyticsCards) { card ->
                            AnalyticsMetricCard(
                                card = card,
                                modifier = Modifier.width(180.dp)
                            )
                        }
                    }
                }
            }

            // Spending Chart Placeholder
            item {
                Column {
                    SectionHeader(
                        title = "Spending Trends",
                        subtitle = "Monthly overview"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.BarChart,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Spending Chart",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Chart implementation goes here",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Category Breakdown
            item {
                Column {
                    SectionHeader(
                        title = "Category Breakdown",
                        subtitle = "Expense distribution"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            categoryBreakdown.forEach { (category, percentage, color) ->
                                CategoryBreakdownItem(
                                    category = category,
                                    percentage = percentage,
                                    color = color
                                )
                                if (category != categoryBreakdown.last().first) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Financial Goals Progress
            item {
                Column {
                    SectionHeader(
                        title = "Goals Progress",
                        subtitle = "Your financial targets"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GoalsProgressCard()
                }
            }

            // Insights Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "AI Insights",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "• You're spending 15% less on dining compared to last month\n" +
                            "• Your savings rate improved by 5% this month\n" +
                            "• Consider setting a budget for entertainment to optimize spending",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AnalyticsMetricCard(
    card: AnalyticsCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = card.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(card.color.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card.icon,
                        contentDescription = null,
                        tint = card.color,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = card.change,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = if (card.isPositive) Color.Green else Color.Red
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = card.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = card.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoryBreakdownItem(
    category: String,
    percentage: Double,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = category,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${percentage}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun GoalsProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            listOf(
                Triple("Emergency Fund", 0.75, "₹75,000 / ₹100,000"),
                Triple("Vacation Fund", 0.45, "₹22,500 / ₹50,000"),
                Triple("New Laptop", 0.90, "₹90,000 / ₹100,000")
            ).forEach { (goal, progress, amount) ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = goal,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = progress.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (goal != "New Laptop") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
