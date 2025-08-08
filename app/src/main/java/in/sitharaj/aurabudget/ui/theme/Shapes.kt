package `in`.sitharaj.aurabudget.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AuraBudgetShapes = Shapes(
    // Extra Small - for chips, badges
    extraSmall = RoundedCornerShape(4.dp),

    // Small - for buttons, small cards
    small = RoundedCornerShape(8.dp),

    // Medium - for cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large - for bottom sheets, large cards
    large = RoundedCornerShape(16.dp),

    // Extra Large - for app bars, major containers
    extraLarge = RoundedCornerShape(28.dp)
)
