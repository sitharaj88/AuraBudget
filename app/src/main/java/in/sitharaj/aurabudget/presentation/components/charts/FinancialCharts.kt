package `in`.sitharaj.aurabudget.presentation.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

/**
 * Beautiful animated pie chart for expense categories
 */
@Composable
fun ExpensePieChart(
    data: List<Pair<String, Double>>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.second }

    if (total == 0.0) {
        EmptyChartState(modifier = modifier)
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Spending Breakdown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                // Pie Chart
                Canvas(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(8.dp)
                ) {
                    drawPieChart(data, colors, total)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                ) {
                    data.forEachIndexed { index, (category, amount) ->
                        val percentage = (amount / total * 100).toFloat()
                        ChartLegendItem(
                            color = colors[index % colors.size],
                            label = category,
                            value = String.format("$%.0f", amount),
                            percentage = String.format("%.1f%%", percentage)
                        )
                        if (index < data.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Animated line chart for spending trends
 */
@Composable
fun SpendingTrendChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { it.second } ?: 0.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Spending Trend",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                drawLineChart(data, maxValue)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach { (label, _) ->
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * Beautiful progress chart for budget tracking
 */
@Composable
fun BudgetProgressChart(
    budgetName: String,
    spent: Double,
    budget: Double,
    modifier: Modifier = Modifier
) {
    val progress = if (budget > 0) (spent / budget).toFloat() else 0f
    val isOverBudget = spent > budget

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOverBudget)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = budgetName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Spent: ${String.format("$%.2f", spent)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Budget: ${String.format("$%.2f", budget)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOverBudget)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = minOf(progress, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isOverBudget)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )

            if (isOverBudget) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠️ Over budget by ${String.format("$%.2f", spent - budget)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Helper functions for drawing charts
private fun DrawScope.drawPieChart(
    data: List<Pair<String, Double>>,
    colors: List<Color>,
    total: Double
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2.5f
    var startAngle = -90f

    data.forEachIndexed { index, (_, value) ->
        val sweepAngle = (value / total * 360).toFloat()
        val color = colors[index % colors.size]

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        startAngle += sweepAngle
    }
}

private fun DrawScope.drawLineChart(
    data: List<Pair<String, Double>>,
    maxValue: Double
) {
    if (data.isEmpty() || maxValue == 0.0) return

    val strokeWidth = 4.dp.toPx()
    val pointRadius = 6.dp.toPx()
    val color = Color(0xFF6200EE)

    val points = data.mapIndexed { index, (_, value) ->
        val x = (index.toFloat() / (data.size - 1)) * size.width
        val y = size.height - (value.toFloat() / maxValue.toFloat()) * size.height
        Offset(x, y)
    }

    // Draw line
    for (i in 0 until points.size - 1) {
        drawLine(
            color = color,
            start = points[i],
            end = points[i + 1],
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }

    // Draw points
    points.forEach { point ->
        drawCircle(
            color = color,
            radius = pointRadius,
            center = point
        )
        drawCircle(
            color = Color.White,
            radius = pointRadius - 2.dp.toPx(),
            center = point
        )
    }
}

@Composable
fun ChartLegendItem(
    color: Color,
    label: String,
    value: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                Text(
                    text = value,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($percentage)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EmptyChartState(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Data Available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Start adding expenses to see your analytics",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

/**
 * Interactive bar chart for comparing categories
 */
@Composable
fun CategoryComparisonChart(
    data: List<Triple<String, Double, Double>>, // Category, Current, Previous
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { maxOf(it.second, it.third) } ?: 1.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Category Comparison",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Current vs Previous Month",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            data.forEach { (category, current, previous) ->
                CategoryBarItem(
                    category = category,
                    currentAmount = current,
                    previousAmount = previous,
                    maxValue = maxValue
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Current",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Previous",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBarItem(
    category: String,
    currentAmount: Double,
    previousAmount: Double,
    maxValue: Double,
    modifier: Modifier = Modifier
) {
    val currentProgress = if (maxValue > 0) (currentAmount / maxValue).toFloat() else 0f
    val previousProgress = if (maxValue > 0) (previousAmount / maxValue).toFloat() else 0f
    val change = currentAmount - previousAmount
    val changePercent = if (previousAmount > 0) (change / previousAmount * 100) else 0.0

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = category,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = String.format("$%.0f", currentAmount),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (change >= 0) "+${changePercent.toInt()}%" else "${changePercent.toInt()}%",
                    fontSize = 10.sp,
                    color = if (change >= 0) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            // Previous month bar (background)
            LinearProgressIndicator(
                progress = previousProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Current month bar (foreground)
            LinearProgressIndicator(
                progress = currentProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.Transparent
            )
        }
    }
}

/**
 * Donut chart variant for better visual appeal
 */
@Composable
fun ExpenseDonutChart(
    data: List<Pair<String, Double>>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.second }

    if (total == 0.0) {
        EmptyChartState(modifier = modifier)
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Expense Distribution",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(200.dp)
                ) {
                    drawDonutChart(data, colors, total)
                }

                // Center text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = String.format("$%.0f", total),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Compact legend
            data.take(5).forEachIndexed { index, (category, amount) ->
                val percentage = (amount / total * 100).toFloat()
                ChartLegendItem(
                    color = colors[index % colors.size],
                    label = category,
                    value = String.format("$%.0f", amount),
                    percentage = String.format("%.1f%%", percentage)
                )
                if (index < minOf(data.size - 1, 4)) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

private fun DrawScope.drawDonutChart(
    data: List<Pair<String, Double>>,
    colors: List<Color>,
    total: Double
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2.2f
    val strokeWidth = radius * 0.3f
    var startAngle = -90f

    data.forEachIndexed { index, (_, value) ->
        val sweepAngle = (value / total * 360).toFloat()
        val color = colors[index % colors.size]

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        startAngle += sweepAngle
    }
}
