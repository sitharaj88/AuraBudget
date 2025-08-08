package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.components.*
import `in`.sitharaj.aurabudget.presentation.navigation.Screen
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedDashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Sample data - this should come from your ViewModels
    val balance = 45000.0
    val income = 75000.0
    val expenses = 30000.0

    val recentTransactions = listOf(
        Transaction(
            id = 1,
            amount = 2500.0,
            category = "Groceries",
            description = "Weekly shopping",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.ShoppingCart
        ),
        Transaction(
            id = 2,
            amount = 50000.0,
            category = "Salary",
            description = "Monthly salary",
            type = TransactionType.INCOME,
            categoryIcon = Icons.Default.Work
        ),
        Transaction(
            id = 3,
            amount = 1200.0,
            category = "Transportation",
            description = "Fuel",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.DirectionsCar
        ),
        Transaction(
            id = 4,
            amount = 800.0,
            category = "Entertainment",
            description = "Movie tickets",
            type = TransactionType.EXPENSE,
            categoryIcon = Icons.Default.Movie
        )
    )

    val categorySpending = listOf(
        Triple("Food & Dining", 8500.0, 12000.0) to Pair(Icons.Default.Restaurant, Color(0xFF4CAF50)),
        Triple("Transportation", 4200.0, 5000.0) to Pair(Icons.Default.DirectionsCar, Color(0xFF2196F3)),
        Triple("Shopping", 6800.0, 8000.0) to Pair(Icons.Default.ShoppingBag, Color(0xFFFF9800)),
        Triple("Entertainment", 2100.0, 3000.0) to Pair(Icons.Default.Movie, Color(0xFF9C27B0))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top App Bar with Enhanced Settings
        EnhancedTopAppBar(
            navController = navController,
            title = {
                Column {
                    Text("Good morning!")
                    Text(
                        "Welcome back",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Balance Card
            item {
                BalanceCard(
                    balance = balance,
                    income = income,
                    expenses = expenses,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onCardClick = { navController.navigate(Screen.Analytics.route) }
                )
            }

            // Quick Actions
            item {
                Column {
                    SectionHeader(
                        title = "Quick Actions",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    QuickActionsRow(
                        onAddIncome = { navController.navigate(Screen.AddTransaction.route + "?type=income") },
                        onAddExpense = { navController.navigate(Screen.AddTransaction.route + "?type=expense") },
                        onTransfer = { navController.navigate(Screen.AddTransaction.route + "?type=transfer") },
                        onBudget = { navController.navigate(Screen.Budget.route) }
                    )
                }
            }

            // Category Spending Overview
            item {
                Column {
                    SectionHeader(
                        title = "Budget Overview",
                        subtitle = "This month's spending",
                        actionText = "View All",
                        onActionClick = { navController.navigate(Screen.Budget.route) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categorySpending) { (categoryData, iconColor) ->
                            val (name, spent, budget) = categoryData
                            val (icon, color) = iconColor
                            CategorySpendingCard(
                                categoryName = name,
                                amount = spent,
                                budget = budget,
                                icon = icon,
                                color = color,
                                modifier = Modifier.width(280.dp)
                            )
                        }
                    }
                }
            }

            // Recent Transactions
            item {
                SectionHeader(
                    title = "Recent Transactions",
                    subtitle = "Last ${recentTransactions.size} transactions",
                    actionText = "See All",
                    onActionClick = { navController.navigate(Screen.Transactions.route) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Add individual transaction items directly to the main LazyColumn
            items(recentTransactions.take(4)) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onTransactionClick = {
                        navController.navigate(Screen.TransactionDetail.createRoute(transaction.id))
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Financial Insights Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Financial Insight",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "You're spending 15% less on dining out this month. Keep it up!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { navController.navigate(Screen.Analytics.route) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text("View Analytics")
                        }
                    }
                }
            }

            // Bottom spacing for navigation bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTopAppBar(
    navController: NavController,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMoreMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    if (showSearchBar) {
        // Search Top App Bar with Material Design 3 styling
        TopAppBar(
            title = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { 
                        Text(
                            "Search transactions...",
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    showSearchBar = false
                    searchQuery = ""
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Close search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) {
                        navController.navigate("${Screen.Transactions.route}?search=$searchQuery")
                    }
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = modifier
        )
    } else {
        // Enhanced Top App Bar with beautiful Material Design 3 styling
        TopAppBar(
            title = title,
            actions = {
                // Search Button with better styling
                FilledTonalIconButton(
                    onClick = { showSearchBar = true },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Quick Filter Button
                FilledTonalIconButton(
                    onClick = { navController.navigate("${Screen.Transactions.route}?filter=recent") },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Notifications with badge indicator
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text("3", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                ) {
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // More Options Menu with beautiful dropdown
                Box {
                    IconButton(onClick = { showMoreMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = { showMoreMenu = false },
                        shape = MaterialTheme.shapes.large,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shadowElevation = 8.dp,
                        tonalElevation = 3.dp
                    ) {
                        // Theme Toggle
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Toggle Theme",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                // TODO: Toggle theme via ViewModel
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Brightness4,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        // Export Data
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Export Data",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                // TODO: Implement export functionality
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        )

                        // Backup & Sync
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Backup & Sync",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                // TODO: Navigate to backup settings
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Backup,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Categories Management
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Manage Categories",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                navController.navigate(Screen.Categories.route)
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Category,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        // Financial Goals
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Financial Goals",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                navController.navigate("goals")
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = IncomeGreen
                                )
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Profile
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Profile",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                navController.navigate(Screen.Profile.route)
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        )

                        // Settings
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Settings",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                navController.navigate(Screen.Settings.route)
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )

                        // Help & Support
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Help & Support",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                showMoreMenu = false
                                // TODO: Navigate to help screen
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Help,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = modifier
        )
    }
}
