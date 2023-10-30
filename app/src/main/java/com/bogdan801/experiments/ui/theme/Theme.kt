package com.bogdan801.experiments.ui.theme


import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = green200,
    primaryVariant = green100,
    secondary = orange,
    onPrimary = darkGray,
    onSecondary = lightGray,
    background = gray
)

@Composable
fun ExperimentsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}