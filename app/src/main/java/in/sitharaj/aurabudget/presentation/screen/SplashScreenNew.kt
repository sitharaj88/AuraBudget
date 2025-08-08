package `in`.sitharaj.aurabudget.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import `in`.sitharaj.aurabudget.R

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    var startFadeOut by remember { mutableStateOf(false) }

    // Enhanced spring animations for smoother feel
    val scaleAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = 0.001f
        ),
        label = "scale"
    )

    val logoAlphaAnimation by animateFloatAsState(
        targetValue = if (startAnimation && !startFadeOut) 1f else 0f,
        animationSpec = if (startFadeOut) {
            tween(
                durationMillis = 600,
                easing = FastOutLinearInEasing
            )
        } else {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "logoAlpha"
    )

    val textScaleAnimation by animateFloatAsState(
        targetValue = if (showText && !startFadeOut) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "textScale"
    )

    val textAlphaAnimation by animateFloatAsState(
        targetValue = if (showText && !startFadeOut) 1f else 0f,
        animationSpec = if (startFadeOut) {
            tween(
                durationMillis = 400,
                easing = FastOutLinearInEasing
            )
        } else {
            tween(
                durationMillis = 800,
                easing = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
            )
        },
        label = "textAlpha"
    )

    val taglineScaleAnimation by animateFloatAsState(
        targetValue = if (showTagline && !startFadeOut) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "taglineScale"
    )

    val taglineAlphaAnimation by animateFloatAsState(
        targetValue = if (showTagline && !startFadeOut) 1f else 0f,
        animationSpec = if (startFadeOut) {
            tween(
                durationMillis = 300,
                easing = FastOutLinearInEasing
            )
        } else {
            tween(
                durationMillis = 600,
                delayMillis = 200,
                easing = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
            )
        },
        label = "taglineAlpha"
    )

    // Smooth continuous rotation
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 20000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val backgroundAlphaAnimation by animateFloatAsState(
        targetValue = if (startFadeOut) 0f else 1f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutLinearInEasing
        ),
        label = "backgroundAlpha"
    )

    LaunchedEffect(Unit) {
        delay(100) // Small initial delay for smooth start
        startAnimation = true
        delay(800) // Wait for logo animation
        showText = true
        delay(600) // Wait for text animation
        showTagline = true
        delay(1200) // Display time
        startFadeOut = true
        delay(600) // Wait for fade out
        onNavigateToMain()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF667EEA).copy(alpha = backgroundAlphaAnimation),
                        Color(0xFF764BA2).copy(alpha = backgroundAlphaAnimation),
                        Color(0xFF6B73FF).copy(alpha = backgroundAlphaAnimation)
                    ),
                    radius = 1500f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Enhanced background sparkles with smoother animations
        SparkleDots(visible = !startFadeOut)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scaleAnimation)
                .alpha(logoAlphaAnimation)
        ) {
            // Main Logo Container with enhanced glow effect
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(pulseAnimation)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            radius = 300f
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Inner circle with enhanced design
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.98f),
                                    Color.White.copy(alpha = 0.92f),
                                    Color.White.copy(alpha = 0.85f)
                                ),
                                radius = 120f
                            ),
                            CircleShape
                        )
                        .rotate(rotationAnimation * 0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    // Enhanced logo content
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "AURA",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4F46E5),
                            letterSpacing = 3.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier
                                    .size(18.dp)
                                    .scale(pulseAnimation * 0.8f + 0.2f)
                            )
                            Text(
                                text = "BUDGET",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981),
                                letterSpacing = 1.5.sp
                            )
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(rotationAnimation * 0.3f)
                                    .scale(pulseAnimation * 0.6f + 0.4f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Enhanced app name with better animation
            Text(
                text = "AuraBudget",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .scale(textScaleAnimation)
                    .alpha(textAlphaAnimation)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Enhanced tagline with card design
            Card(
                modifier = Modifier
                    .scale(taglineScaleAnimation)
                    .alpha(taglineAlphaAnimation)
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Your Financial Journey Starts Here",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp)
                )
            }
        }

        // Enhanced loading indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .alpha(logoAlphaAnimation)
        ) {
            CircularProgressIndicator(
                color = Color.White.copy(alpha = 0.8f),
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(28.dp)
                    .scale(pulseAnimation * 0.2f + 0.8f)
            )
        }
    }
}

@Composable
private fun SparkleDots(visible: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")

    val sparkle1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle1"
    )

    val sparkle2Alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle2"
    )

    if (visible) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Sparkle dots positioned around the screen
            listOf(
                Offset(0.15f, 0.2f) to sparkle1Alpha,
                Offset(0.85f, 0.15f) to sparkle2Alpha,
                Offset(0.1f, 0.7f) to sparkle2Alpha,
                Offset(0.9f, 0.75f) to sparkle1Alpha,
                Offset(0.25f, 0.85f) to sparkle1Alpha,
                Offset(0.75f, 0.85f) to sparkle2Alpha
            ).forEach { (offset, alpha) ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.TopStart)
                        .offset(
                            x = (offset.x * 300).dp,
                            y = (offset.y * 600).dp
                        )
                        .size(4.dp)
                        .background(
                            Color.White.copy(alpha = alpha),
                            CircleShape
                        )
                )
            }
        }
    }
}

private data class Offset(val x: Float, val y: Float)
