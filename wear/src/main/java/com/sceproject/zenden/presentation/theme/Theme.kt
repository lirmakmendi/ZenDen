package com.sceproject.zenden.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

internal val wearColorPalette: Colors = Colors(
    primary = Color(0xFF1E88E5), // A vibrant blue
    primaryVariant = Color(0xFF1565C0), // A deeper shade of blue
    secondary = Color(0xFFC2185B), // A complementary secondary color, vibrant pink
    secondaryVariant = Color(0xFFAD1457), // A deeper shade of pink
    background = Color(0xFF121212), // Dark background for better contrast and modern look
    surface = Color(0xFF1E1E1E), // Slightly lighter than background for elevation effect
    error = Color(0xFFD32F2F), // Standard error color for clear visibility
    onPrimary = Color.White, // Text color on primary backgrounds
    onSecondary = Color.White, // Text color on secondary backgrounds
    onBackground = Color.White, // Text color on background
    onSurface = Color.White, // Text color on surface
    onError = Color.White // Text color on error backgrounds
)


@Composable
fun ZenDenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = wearColorPalette,
        content = content
    )
}