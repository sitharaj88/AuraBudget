package `in`.sitharaj.aurabudget.presentation.screen.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.sitharaj.aurabudget.presentation.viewmodel.SetupViewModel

/**
 * Setup Screen - Initial app configuration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentStep by remember { mutableIntStateOf(0) }

    val steps = listOf(
        "Personal Info",
        "Currency & Income",
        "Categories",
        "Initial Budget"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Setup AuraBudget",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Progress Indicator
            SetupProgressIndicator(
                currentStep = currentStep,
                totalSteps = steps.size,
                stepLabels = steps
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Step Content
            when (currentStep) {
                0 -> PersonalInfoStep(
                    uiState = uiState,
                    onNameChange = viewModel::updateName,
                    onEmailChange = viewModel::updateEmail
                )
                1 -> CurrencyIncomeStep(
                    uiState = uiState,
                    onCurrencyChange = viewModel::updateCurrency,
                    onIncomeChange = viewModel::updateMonthlyIncome
                )
                2 -> CategoriesStep(
                    uiState = uiState,
                    onCategoryToggle = viewModel::toggleCategory
                )
                3 -> InitialBudgetStep(
                    uiState = uiState,
                    onBudgetChange = viewModel::updateCategoryBudget
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- }
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Button(
                    onClick = {
                        if (currentStep < steps.size - 1) {
                            currentStep++
                        } else {
                            viewModel.completeSetup()
                            navController.navigate("dashboard") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    },
                    enabled = viewModel.isStepValid(currentStep)
                ) {
                    Text(if (currentStep < steps.size - 1) "Next" else "Complete Setup")
                }
            }
        }
    }
}

@Composable
fun SetupProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    stepLabels: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Progress Bar
        LinearProgressIndicator(
            progress = { (currentStep + 1f) / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stepLabels[currentStep],
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "${currentStep + 1} of $totalSteps",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PersonalInfoStep(
    uiState: `in`.sitharaj.aurabudget.presentation.viewmodel.SetupUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Let's get to know you",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Email (Optional)") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
    }
}

@Composable
fun CurrencyIncomeStep(
    uiState: `in`.sitharaj.aurabudget.presentation.viewmodel.SetupUiState,
    onCurrencyChange: (String) -> Unit,
    onIncomeChange: (String) -> Unit
) {
    val currencies = listOf("USD", "EUR", "GBP", "CAD", "AUD", "INR", "JPY")
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Set your currency and monthly income",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        // Currency Selector
        @OptIn(ExperimentalMaterial3Api::class)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = uiState.currency,
                onValueChange = { },
                readOnly = true,
                label = { Text("Currency") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            @OptIn(ExperimentalMaterial3Api::class)
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            onCurrencyChange(currency)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.monthlyIncome,
            onValueChange = onIncomeChange,
            label = { Text("Monthly Income") },
            leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun CategoriesStep(
    uiState: `in`.sitharaj.aurabudget.presentation.viewmodel.SetupUiState,
    onCategoryToggle: (String) -> Unit
) {
    val defaultCategories = listOf(
        CategoryItem("Food & Dining", Icons.Default.Restaurant),
        CategoryItem("Transportation", Icons.Default.DirectionsCar),
        CategoryItem("Shopping", Icons.Default.ShoppingCart),
        CategoryItem("Entertainment", Icons.Default.Movie),
        CategoryItem("Bills & Utilities", Icons.Default.Receipt),
        CategoryItem("Healthcare", Icons.Default.LocalHospital),
        CategoryItem("Travel", Icons.Default.Flight),
        CategoryItem("Education", Icons.Default.School),
        CategoryItem("Groceries", Icons.Default.LocalGroceryStore),
        CategoryItem("Fitness", Icons.Default.FitnessCenter)
    )

    Column {
        Text(
            text = "Choose your expense categories",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(defaultCategories) { category ->
                CategorySelectionCard(
                    category = category,
                    isSelected = uiState.selectedCategories.contains(category.name),
                    onToggle = { onCategoryToggle(category.name) }
                )
            }
        }
    }
}

@Composable
fun InitialBudgetStep(
    uiState: `in`.sitharaj.aurabudget.presentation.viewmodel.SetupUiState,
    onBudgetChange: (String, String) -> Unit
) {
    Column {
        Text(
            text = "Set initial budgets for your categories",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.selectedCategories) { category ->
                OutlinedTextField(
                    value = uiState.categoryBudgets[category] ?: "",
                    onValueChange = { onBudgetChange(category, it) },
                    label = { Text("$category Budget") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
fun CategorySelectionCard(
    category: CategoryItem,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onToggle,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = category.name,
                modifier = Modifier.weight(1f),
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

data class CategoryItem(
    val name: String,
    val icon: ImageVector
)
