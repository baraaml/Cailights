package com.example.cailights.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CaiDarkPrimary,
    secondary = CaiDarkSecondary,
    tertiary = CaiDarkTertiary,
    background = CaiDarkBackground,
    surface = CaiDarkSurface,
    surfaceVariant = CaiDarkSurfaceVariant,
    onPrimary = CaiDarkBackground,
    onSecondary = CaiDarkBackground,
    onTertiary = CaiDarkBackground,
    onBackground = CaiDarkText,
    onSurface = CaiDarkText,
    onSurfaceVariant = CaiDarkTextSecondary,
    outline = BrandLightBlue.copy(alpha = 0.3f)
)

private val LightColorScheme = lightColorScheme(
    primary = CaiLightPrimary,
    secondary = CaiLightSecondary,
    tertiary = CaiLightTertiary,
    background = CaiLightBackground,
    surface = CaiLightSurface,
    surfaceVariant = CaiLightSurfaceVariant,
    onPrimary = CaiLightSurface,
    onSecondary = CaiLightText,
    onTertiary = CaiLightText,
    onBackground = CaiLightText,
    onSurface = CaiLightText,
    onSurfaceVariant = CaiLightTextSecondary,
    outline = BrandMarianBlue.copy(alpha = 0.3f)
)

@Composable
fun CailightsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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