package `in`.sitharaj.aurabudget.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val description: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val type: TransactionType,
    val categoryIcon: ImageVector = Icons.Default.ShoppingCart
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTransactionClick(transaction) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        when (transaction.type) {
                            TransactionType.INCOME -> Color.Green.copy(alpha = 0.2f)
                            TransactionType.EXPENSE -> Color.Red.copy(alpha = 0.2f)
                            TransactionType.TRANSFER -> Color.Blue.copy(alpha = 0.2f)
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transaction.categoryIcon,
                    contentDescription = transaction.category,
                    tint = when (transaction.type) {
                        TransactionType.INCOME -> Color.Green
                        TransactionType.EXPENSE -> Color.Red
                        TransactionType.TRANSFER -> Color.Blue
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Transaction Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.category,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (transaction.description.isNotBlank()) {
                    Text(
                        text = transaction.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = transaction.date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}₹${String.format("%.2f", transaction.amount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (transaction.type) {
                        TransactionType.INCOME -> Color.Green
                        TransactionType.EXPENSE -> Color.Red
                        TransactionType.TRANSFER -> MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionsList(
    transactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
    showHeader: Boolean = true
) {
    Column(modifier = modifier) {
        if (showHeader) {
            SectionHeader(
                title = "Recent Transactions",
                subtitle = "Last ${transactions.size} transactions",
                actionText = "See All",
                onActionClick = { /* Navigate to all transactions */ },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onTransactionClick = onTransactionClick
                )
            }
        }
    }
}

@Composable
fun CategorySpendingCard(
    categoryName: String,
    amount: Double,
    budget: Double,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = (amount / budget).coerceAtMost(1.0)

    Card(
        modifier = modifier.fillMaxWidth(),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = categoryName,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = categoryName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "₹${String.format("%.0f", amount)} of ₹${String.format("%.0f", budget)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (progress > 0.8f) Color.Red else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progress.toFloat() > 0.8f) Color.Red else color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}
