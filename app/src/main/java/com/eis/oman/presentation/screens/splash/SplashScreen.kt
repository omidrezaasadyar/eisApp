package com.eis.oman.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eis.oman.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FLAG_TARGET_ALPHA = 0.10f
private const val GEAR_ROTATION_PERIOD_MS = 8_000
private const val SPLASH_HOLD_MS = 1_400L

@Composable
fun SplashScreen(onReady: () -> Unit) {
    val flagAlpha = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.7f) }
    val textAlpha = remember { Animatable(0f) }

    val gearRotation by rememberInfiniteTransition(label = "gear").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = GEAR_ROTATION_PERIOD_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "gearRotation",
    )

    LaunchedEffect(Unit) {
        launch { flagAlpha.animateTo(FLAG_TARGET_ALPHA, tween(700, easing = FastOutSlowInEasing)) }
        launch { logoAlpha.animateTo(1f, tween(550, delayMillis = 150)) }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
            )
        }
        launch { textAlpha.animateTo(1f, tween(500, delayMillis = 500)) }

        delay(SPLASH_HOLD_MS + 1_400L)
        onReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.bg_oman_flag),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(flagAlpha.value),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(220.dp)
                        .rotate(gearRotation)
                        .alpha(0.18f),
                )
                Image(
                    painter = painterResource(R.drawable.ic_eis_logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(140.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value),
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.brand_full_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.brand_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value),
            )

            Spacer(Modifier.height(48.dp))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(28.dp)
                    .alpha(textAlpha.value),
            )
        }
    }
}
