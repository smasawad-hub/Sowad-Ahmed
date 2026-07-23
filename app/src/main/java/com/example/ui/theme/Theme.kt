package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SphereDarkColorScheme = darkColorScheme(
    primary = SphereBlue,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = Color.White,
    secondary = SpherePurple,
    onSecondary = Color.White,
    tertiary = SphereCyan,
    background = DarkBackground,
    onBackground = OnDarkTextPrimary,
    surface = DarkSurface,
    onSurface = OnDarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkTextSecondary,
    outline = DarkBorder
)

private val SphereLightColorScheme = lightColorScheme(
    primary = SphereBlue,
    onPrimary = Color.White,
    primaryContainer = LightSurfaceVariant,
    onPrimaryContainer = OnLightTextPrimary,
    secondary = SpherePurple,
    onSecondary = Color.White,
    tertiary = SphereCyan,
    background = LightBackground,
    onBackground = OnLightTextPrimary,
    surface = LightSurface,
    onSurface = OnLightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightTextSecondary,
    outline = LightBorder
)

@Composable
fun SphereTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SphereDarkColorScheme else SphereLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for backwards compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SphereTheme(darkTheme = darkTheme, content = content)
}
