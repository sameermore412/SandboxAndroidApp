package com.more.sandboxapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = primary,
    primaryVariant = primaryDark,
    secondary = secondary,
    secondaryVariant = secondaryDark,
    background = primaryLight,
    surface = secondaryLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.Black,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = primary,
    primaryVariant = primaryDark,
    secondary = secondary,
    secondaryVariant = secondaryDark,
    background = primaryLight,
    surface = secondaryLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.Black,
    onBackground = Color.White
)

@Composable
fun SandboxAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}