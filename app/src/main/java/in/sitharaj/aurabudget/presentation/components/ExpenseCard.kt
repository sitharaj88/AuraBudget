package `in`.sitharaj.aurabudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Beautiful, modern expense item component
 * Following Single Responsibility Principle
 */
@Composable
fun ExpenseCard(
    expense: ExpenseEntity,
    onEdit: (ExpenseEntity) -> Unit = {},
    onDelete: (ExpenseEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onEdit(expense) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = expense.description ?: "No description",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = dateFormat.format(Date(expense.date)),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    if (expense.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            expense.tags.take(3).forEach { tag ->
                                TagChip(tag = tag)
                            }
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = expense.getFormattedAmount(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onEdit(expense) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { onDelete(expense) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TagChip(tag: String) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
