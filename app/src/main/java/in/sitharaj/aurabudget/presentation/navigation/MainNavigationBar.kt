package `in`.sitharaj.aurabudget.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import `in`.sitharaj.aurabudget.ui.theme.NavigationBackground
import `in`.sitharaj.aurabudget.ui.theme.NavigationIndicator
import `in`.sitharaj.aurabudget.ui.theme.NavigationSelected
import `in`.sitharaj.aurabudget.ui.theme.NavigationUnselected

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom navigation only on main screens
    val showBottomNav = Screen.bottomNavItems.any { it.route == currentRoute }

    AnimatedVisibility(
        visible = showBottomNav,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = NavigationBackground,
            contentColor = NavigationSelected,
            tonalElevation = 8.dp,
            windowInsets = WindowInsets(0.dp)
        ) {
            Screen.bottomNavItems.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon ?: return@NavigationBarItem,
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (currentRoute == screen.route) FontWeight.SemiBold else FontWeight.Medium
                        )
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination to avoid building up a large stack
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavigationSelected,
                        selectedTextColor = NavigationSelected,
                        indicatorColor = NavigationIndicator.copy(alpha = 0.15f),
                        unselectedIconColor = NavigationUnselected,
                        unselectedTextColor = NavigationUnselected
                    ),
                    modifier = Modifier.clip(MaterialTheme.shapes.large)
                )
            }
        }
    }
}
