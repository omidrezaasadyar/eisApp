package com.eis.oman.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = BrandOrange,
    onPrimary = SurfaceDark,
    primaryContainer = BrandOrangeDeep,
    onPrimaryContainer = OffWhite,
    secondary = BrandOrange,
    onSecondary = SurfaceDark,
    secondaryContainer = BrandOrangeDeep,
    onSecondaryContainer = OffWhite,
    tertiary = BrandOrangeSoft,
    onTertiary = SurfaceDark,
    background = SurfaceDark,
    onBackground = InkPrimary,
    surface = SurfaceDark,
    onSurface = InkPrimary,
    surfaceVariant = SurfaceDarkVariant,
    onSurfaceVariant = InkSecondary,
    surfaceContainer = SurfaceDarkVariant,
    surfaceContainerHigh = SurfaceDarkLight,
    surfaceContainerHighest = SurfaceDarkLight,
    outline = InkMuted,
    outlineVariant = SurfaceDarkLight,
    error = ErrorRed,
    onError = OffWhite,
)

private val LightColors = lightColorScheme(
    primary = BrandOrangeDeep,
    onPrimary = OffWhite,
    primaryContainer = BrandOrangeSoft,
    onPrimaryContainer = SurfaceDark,
    secondary = BrandOrangeDeep,
    onSecondary = OffWhite,
    secondaryContainer = BrandOrangeSoft,
    onSecondaryContainer = SurfaceDark,
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
