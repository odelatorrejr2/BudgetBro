package com.example.budgetbro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ChristmasLight = lightColorScheme(
    primary = PineGreen,
    onPrimary = Color.White,
    primaryContainer = Evergreen,
    onPrimaryContainer = Color.White,

    secondary = HollyRed,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDADA),
    onSecondaryContainer = Color(0xFF3B0A0A),

    tertiary = Gold,
    onTertiary = Color(0xFF2B2000),

    background = LightGreyBg,
    onBackground = Color(0xFF1A1C1E),

    surface = LightCard,
    onSurface = Color(0xFF1A1C1E),

    error = HollyRed
)

private val ChristmasDark = darkColorScheme(
    primary = PineGreen,
    onPrimary = Color.White,
    primaryContainer = Evergreen,
    onPrimaryContainer = Color.White,

    secondary = HollyRed,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF5A1A1A),
    onSecondaryContainer = Color(0xFFFFDADA),

    tertiary = Gold,
    onTertiary = Color(0xFF2B2000),

    background = Midnight,
    onBackground = Color(0xFFE6E6E6),

    surface = Color(0xFF15171C),
    onSurface = Color(0xFFE6E6E6),

    error = HollyRed
)

@Composable
fun BudgetBroTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) ChristmasDark else ChristmasLight

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
