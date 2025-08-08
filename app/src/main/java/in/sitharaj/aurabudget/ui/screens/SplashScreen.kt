package `in`.sitharaj.aurabudget.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash delay
        onNavigateToMain()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App logo/icon
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Using system icon for now
            contentDescription = "AuraBudget Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AuraBudget",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Smart Finance Management",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 3.dp
        )
    }
}
