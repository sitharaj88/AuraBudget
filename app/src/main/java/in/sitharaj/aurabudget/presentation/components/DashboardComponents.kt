package `in`.sitharaj.aurabudget.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Beautiful financial summary card for dashboard
 * Following Single Responsibility Principle
 */
@Composable
fun FinancialSummaryCard(
    title: String,
    amount: Double,
    subtitle: String? = null,
    trend: String? = null,
    isPositive: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format("$%.2f", amount),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (subtitle != null || trend != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }

                    if (trend != null) {
                        Text(
                            text = trend,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Category spending breakdown component
 */
@Composable
fun CategorySpendingItem(
    categoryName: String,
    amount: Double,
    percentage: Float,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .then(Modifier.fillMaxSize())
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = color),
                    modifier = Modifier.fillMaxSize()
                ) {}
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = categoryName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${String.format("%.1f", percentage)}%",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Text(
            text = String.format("$%.2f", amount),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
