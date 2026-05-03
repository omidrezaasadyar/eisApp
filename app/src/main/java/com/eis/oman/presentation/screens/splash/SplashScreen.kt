package com.eis.oman.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eis.oman.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_HOLD_MS = 1_900L

@Composable
fun SplashScreen(onReady: () -> Unit) {
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.7f) }
    val textAlpha = remember { Animatable(0f) }
    val curveProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { curveProgress.animateTo(1f, tween(1_200, easing = FastOutSlowInEasing)) }
        launch { logoAlpha.animateTo(1f, tween(550, delayMillis = 200)) }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
            )
        }
        launch { textAlpha.animateTo(1f, tween(500, delayMillis = 600)) }

        delay(SPLASH_HOLD_MS)
        onReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SplashTopColor,
                        SplashBottomColor,
                    ),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Subtle dotted grid + decorative red curves
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGrid(spacingPx = 28.dp.toPx(), alpha = 0.06f)
            drawDecorativeCurves(progress = curveProgress.value)
        }

        // Center column: logo + brand name + motto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_eis_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(200.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
            )

            Spacer(Modifier.height(24.dp))

            androidx.compose.material3.Text(
                text = stringResource(R.string.brand_full_name_long),
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value),
            )
            Spacer(Modifier.height(12.dp))
            androidx.compose.material3.Text(
                text = stringResource(R.string.brand_motto),
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value),
            )
        }
    }
}

private val SplashTopColor = Color(0xFF0F1428)
private val SplashBottomColor = Color(0xFF1B1F3A)
private val SplashAccentRed = Color(0xFFE63946)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGrid(
    spacingPx: Float,
    alpha: Float,
) {
    val color = Color.White.copy(alpha = alpha)
    val w = size.width
    val h = size.height
    var x = 0f
    while (x <= w) {
        drawLine(
            color = color,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 0.6f,
        )
        x += spacingPx
    }
    var y = 0f
    while (y <= h) {
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 0.6f,
        )
        y += spacingPx
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDecorativeCurves(
    progress: Float,
) {
    val w = size.width
    val h = size.height

    // Right-side flowing curve (top-right corner, sweeping down-left)
    val rightPath = Path().apply {
        moveTo(w * 0.95f, -20f)
        cubicTo(
            x1 = w * 1.05f, y1 = h * 0.30f,
            x2 = w * 0.55f, y2 = h * 0.55f,
            x3 = w * 0.65f, y3 = h * 0.95f,
        )
    }
    drawPath(
        path = rightPath,
        color = SplashAccentRed.copy(alpha = 0.85f * progress),
        style = Stroke(
            width = 2.4f,
            pathEffect = if (progress < 1f) PathEffect.dashPathEffect(
                floatArrayOf(40f * progress, 9999f),
                phase = 0f,
            ) else null,
        ),
    )

    // Left-side soft dark wave (filled shape, low alpha)
    val leftWave = Path().apply {
        moveTo(0f, h * 0.10f)
        cubicTo(
            x1 = w * 0.45f, y1 = h * 0.35f,
            x2 = w * 0.05f, y2 = h * 0.55f,
            x3 = w * 0.25f, y3 = h * 0.75f,
        )
        cubicTo(
            x1 = w * 0.35f, y1 = h * 0.85f,
            x2 = -w * 0.05f, y2 = h * 0.95f,
            x3 = 0f, y3 = h,
        )
        lineTo(0f, h)
        lineTo(0f, h * 0.10f)
        close()
    }
    drawPath(
        path = leftWave,
        color = Color.White.copy(alpha = 0.04f * progress),
    )

    // Subtler left curve (red, thinner)
    val leftCurve = Path().apply {
        moveTo(w * 0.25f, h * 0.30f)
        cubicTo(
            x1 = -w * 0.10f, y1 = h * 0.55f,
            x2 = w * 0.40f, y2 = h * 0.75f,
            x3 = w * 0.15f, y3 = h + 20f,
        )
    }
    drawPath(
        path = leftCurve,
        color = SplashAccentRed.copy(alpha = 0.55f * progress),
        style = Stroke(
            width = 1.6f,
        ),
    )
}
