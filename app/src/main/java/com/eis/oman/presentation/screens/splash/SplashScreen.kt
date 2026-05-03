package com.eis.oman.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eis.oman.R
import kotlinx.coroutines.delay

private const val SPLASH_HOLD_MS = 2_400L

@Composable
fun SplashScreen(onReady: () -> Unit) {
    val imageAlpha = remember { Animatable(0f) }
    val loaderAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        imageAlpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 500))
        loaderAlpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 400))
        delay(SPLASH_HOLD_MS)
        onReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF171C32)),
    ) {
        Image(
            painter = painterResource(R.drawable.splash_eis),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(imageAlpha.value),
        )

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.White.copy(alpha = 0.12f),
            strokeWidth = 3.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .size(48.dp)
                .alpha(loaderAlpha.value),
        )
    }
}
