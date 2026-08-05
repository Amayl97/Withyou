package com.example.withyou.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = Primary,
    secondary = GoldAccent,
    tertiary = Accent,

    background = WhiteBackground,
    surface = CardSurface,

    onPrimary = WhiteBackground,
    onSecondary = TextPrimary,
    onTertiary = WhiteBackground,

    onBackground = TextPrimary,
    onSurface = TextPrimary,

    error = Accent,
    onError = WhiteBackground
)

private val DarkColorScheme = darkColorScheme(

    primary = DarkPrimary,
    secondary = DarkGoldAccent,
    tertiary = DarkAccent,

    background = DarkBackground,
    surface = DarkSurface,

    onPrimary = DarkBackground,
    onSecondary = DarkTextPrimary,
    onTertiary = DarkBackground,

    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,

    error = DarkAccent,
    onError = DarkBackground
)

@Composable
fun WithyouTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}