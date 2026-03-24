package com.dev.mealapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryExpressive,
    onPrimary = OnPrimaryExpressive,
    primaryContainer = OnPrimaryContainerExpressive,
    onPrimaryContainer = PrimaryContainerExpressive,
    secondary = SecondaryExpressive,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainerExpressive,
    tertiary = TertiaryExpressive,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerExpressive,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryExpressive,
    onPrimary = OnPrimaryExpressive,
    primaryContainer = PrimaryContainerExpressive,
    onPrimaryContainer = OnPrimaryContainerExpressive,
    secondary = SecondaryExpressive,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainerExpressive,
    tertiary = TertiaryExpressive,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerExpressive,
    background = BackgroundExpressive,
    surface = SurfaceExpressive,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = OutlineExpressive
)

@Composable
fun MealAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
