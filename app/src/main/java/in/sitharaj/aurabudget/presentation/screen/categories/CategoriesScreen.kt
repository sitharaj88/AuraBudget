package `in`.sitharaj.aurabudget.presentation.screen.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.viewmodel.CategoryViewModel
import `in`.sitharaj.aurabudget.domain.model.CategoryEntity

/**
 * Category Management Screen - Create, edit, and organize expense categories
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Categories",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddCategoryDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Categories Header with Stats
            item {
                CategoriesOverviewCard(
                    totalCategories = uiState.categories.size,
                    activeCategories = uiState.categories.count { true }, // Assume all active for now
                    mostUsedCategory = uiState.mostUsedCategory
                )
            }
            
            // Default Categories Section
            item {
                SectionHeader("Default Categories")
            }
            
            items(uiState.categories.filter { it.isDefault == true }) { category ->
                CategoryCard(
                    category = category,
                    onEditClick = { viewModel.editCategory(category) },
                    onToggleActive = { viewModel.toggleCategoryActive(category.id) },
                    onDeleteClick = null // Can't delete default categories
                )
            }
            
            // Custom Categories Section
            if (uiState.categories.any { it.isDefault != true }) {
                item {
                    SectionHeader("Custom Categories")
                }
                
                items(uiState.categories.filter { it.isDefault != true }) { category ->
                    CategoryCard(
                        category = category,
                        onEditClick = { viewModel.editCategory(category) },
                        onToggleActive = { viewModel.toggleCategoryActive(category.id) },
                        onDeleteClick = { viewModel.deleteCategory(category.id) }
                    )
                }
            }
            
            // Empty State
            if (uiState.categories.isEmpty()) {
                item {
                    EmptyCategoriesState(
                        onCreateCategory = { showAddCategoryDialog = true }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
    
    // Add Category Dialog
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name, icon, color ->
                viewModel.addCategory(name, icon, color)
                showAddCategoryDialog = false
            }
        )
    }
}

@Composable
fun CategoriesOverviewCard(
    totalCategories: Int,
    activeCategories: Int,
    mostUsedCategory: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Categories Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryMetric(
                    label = "Total",
                    value = totalCategories.toString(),
                    color = MaterialTheme.colorScheme.primary
                )

                CategoryMetric(
                    label = "Active",
                    value = activeCategories.toString(),
                    color = Color(0xFF4CAF50)
                )

                CategoryMetric(
                    label = "Inactive",
                    value = (totalCategories - activeCategories).toString(),
                    color = Color(0xFFFF9800)
                )
            }

            if (mostUsedCategory != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Most used: $mostUsedCategory",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun CategoryMetric(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun CategoryCard(
    category: CategoryEntity,
    onEditClick: () -> Unit,
    onToggleActive: () -> Unit,
    onDeleteClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val isActive = true // Assume active for now since CategoryEntity doesn't have isActive field

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) 
                MaterialTheme.colorScheme.surface 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Color(category.color?.toLong() ?: 0xFF6200EE).copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.icon),
                    contentDescription = null,
                    tint = Color(category.color?.toLong() ?: 0xFF6200EE),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Category Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) 
                        MaterialTheme.colorScheme.onSurface 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (category.isDefault == true) "Default" else "Custom",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    
                    if (!isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Inactive",
                            fontSize = 12.sp,
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }
            
            // Action Buttons
            Row {
                // Toggle Active/Inactive
                IconButton(onClick = onToggleActive) {
                    Icon(
                        imageVector = if (isActive) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isActive) "Deactivate" else "Activate",
                        tint = if (isActive) MaterialTheme.colorScheme.primary else Color(0xFFFF9800)
                    )
                }
                
                // Edit
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                // Delete (only for custom categories)
                if (onDeleteClick != null) {
                    IconButton(onClick = onDeleteClick) {
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
}

@Composable
fun EmptyCategoriesState(
    onCreateCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏷️",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Categories Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create categories to organize your expenses and income",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateCategory
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Category")
            }
        }
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("restaurant") }
    var selectedColor by remember { mutableStateOf(0xFF6200EE) }
    
    val availableIcons = listOf(
        "restaurant" to Icons.Default.Restaurant,
        "directions_car" to Icons.Default.DirectionsCar,
        "shopping_cart" to Icons.Default.ShoppingCart,
        "movie" to Icons.Default.Movie,
        "receipt" to Icons.Default.Receipt,
        "local_hospital" to Icons.Default.LocalHospital,
        "flight" to Icons.Default.Flight,
        "school" to Icons.Default.School,
        "fitness_center" to Icons.Default.FitnessCenter,
        "home" to Icons.Default.Home
    )
    
    val availableColors = listOf(
        0xFF6200EE, 0xFF03DAC6, 0xFFFF6B6B, 0xFF4ECDC4,
        0xFF45B7D1, 0xFF96CEB4, 0xFFFD79A8, 0xFFE84393,
        0xFFFFB74D, 0xFFAED581
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Category") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Icon Selection
                Text(
                    text = "Choose Icon",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableIcons) { (iconName, icon) ->
                        IconButton(
                            onClick = { selectedIcon = iconName },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    if (selectedIcon == iconName) 
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else 
                                        Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (selectedIcon == iconName) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                
                // Color Selection
                Text(
                    text = "Choose Color",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableColors) { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .then(
                                    if (selectedColor == color) {
                                        Modifier.border(
                                            3.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    } else Modifier
                                )
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColor == color) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(categoryName, selectedIcon, selectedColor)
                },
                enabled = categoryName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun getCategoryIcon(iconName: String?): ImageVector {
    return when (iconName) {
        "restaurant" -> Icons.Default.Restaurant
        "directions_car" -> Icons.Default.DirectionsCar
        "shopping_cart" -> Icons.Default.ShoppingCart
        "movie" -> Icons.Default.Movie
        "receipt" -> Icons.Default.Receipt
        "local_hospital" -> Icons.Default.LocalHospital
        "flight" -> Icons.Default.Flight
        "school" -> Icons.Default.School
        "fitness_center" -> Icons.Default.FitnessCenter
        "home" -> Icons.Default.Home
        else -> Icons.Default.Category
    }
}
