package com.zooeydigital.marsrover.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = TextIcons,
    primaryContainer = Primary,
    onPrimaryContainer = TextIcons,
    secondary = Accent,
    onSecondary = TextIcons,
    background = PrimaryText,
    onBackground = TextIcons,
    surface = ColorSurfaceDark,
    onSurface = TextIcons,
    surfaceVariant = SecondaryText,
    onSurfaceVariant = Divider,
    outline = SecondaryText,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextIcons,
    primaryContainer = LightPrimary,
    onPrimaryContainer = PrimaryText,
    secondary = Accent,
    onSecondary = TextIcons,
    background = TextIcons,
    onBackground = PrimaryText,
    surface = TextIcons,
    onSurface = PrimaryText,
    surfaceVariant = LightPrimary,
    onSurfaceVariant = SecondaryText,
    outline = Divider,
)

@Composable
fun MarsRoverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
