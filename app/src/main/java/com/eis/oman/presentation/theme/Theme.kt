package com.eis.oman.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = SteelBlue300,
    onPrimary = Graphite900,
    primaryContainer = SteelBlue700,
    onPrimaryContainer = SteelBlue100,
    secondary = SafetyAmber,
    onSecondary = Graphite900,
    secondaryContainer = SafetyAmberDeep,
    onSecondaryContainer = OffWhite,
    tertiary = SteelBlue500,
    onTertiary = OffWhite,
    background = Graphite900,
    onBackground = OffWhite,
    surface = Graphite800,
    onSurface = OffWhite,
    surfaceVariant = Graphite700,
    onSurfaceVariant = Graphite300,
    surfaceContainer = Graphite700,
    surfaceContainerHigh = Graphite500,
    outline = Graphite500,
    outlineVariant = Graphite700,
    error = ErrorRed,
    onError = OffWhite,
)

private val LightColors = lightColorScheme(
    primary = SteelBlue700,
    onPrimary = OffWhite,
    primaryContainer = SteelBlue100,
    onPrimaryContainer = SteelBlue900,
    secondary = SafetyAmberDeep,
    onSecondary = OffWhite,
    secondaryContainer = SafetyAmber,
    onSecondaryContainer = Graphite900,
    tertiary = SteelBlue500,
    onTertiary = OffWhite,
    background = OffWhite,
    onBackground = Graphite900,
    surface = OffWhite,
    onSurface = Graphite900,
    surfaceVariant = Graphite100,
    onSurfaceVariant = Graphite500,
    surfaceContainer = Graphite100,
    surfaceContainerHigh = Color(0xFFDDE2E8),
    outline = Graphite300,
    outlineVariant = Graphite100,
    error = ErrorRed,
    onError = OffWhite,
)

@Composable
fun EISTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = EISTypography,
        shapes = EISShapes,
        content = content,
    )
}
