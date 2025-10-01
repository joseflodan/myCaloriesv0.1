package com.example.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class AppTheme {
    SAGE,
    PASTEL_BLUE,
    PASTEL_PINK,
    PASTEL_ORANGE,
    PURPLE,
    NAVY,
    AUTUMN_ORANGE
}

private val SageLightColorScheme = lightColorScheme(
    primary = Sage_Primary,
    onPrimary = Sage_OnPrimary,
    secondary = Sage_Secondary,
    background = Sage_Background_Light,
    onBackground = Sage_OnBackground_Light
)

private val SageDarkColorScheme = darkColorScheme(
    primary = Sage_Primary,
    onPrimary = Sage_OnPrimary,
    secondary = Sage_Secondary,
    background = Sage_Background_Dark,
    onBackground = Sage_OnBackground_Dark
)

private val PastelBlueColorScheme = lightColorScheme(
    primary = PastelBlue_Primary,
    onPrimary = PastelBlue_OnPrimary,
    secondary = PastelBlue_Secondary,
    background = PastelBlue_Background,
    onBackground = PastelBlue_OnBackground
)

private val PastelPinkColorScheme = lightColorScheme(
    primary = PastelPink_Primary,
    onPrimary = PastelPink_OnPrimary,
    secondary = PastelPink_Secondary,
    background = PastelPink_Background,
    onBackground = PastelPink_OnBackground
)

private val PastelOrangeColorScheme = lightColorScheme(
    primary = PastelOrange_Primary,
    onPrimary = PastelOrange_OnPrimary,
    secondary = PastelOrange_Secondary,
    background = PastelOrange_Background,
    onBackground = PastelOrange_OnBackground
)

private val PurpleColorScheme = darkColorScheme(
    primary = Purple_Primary,
    onPrimary = Purple_OnPrimary,
    secondary = Purple_Secondary,
    background = Purple_Background,
    onBackground = Purple_OnBackground
)

private val NavyColorScheme = darkColorScheme(
    primary = Navy_Primary,
    onPrimary = Navy_OnPrimary,
    secondary = Navy_Secondary,
    background = Navy_Background,
    onBackground = Navy_OnBackground
)

private val AutumnOrangeColorScheme = darkColorScheme(
    primary = AutumnOrange_Primary,
    onPrimary = AutumnOrange_OnPrimary,
    secondary = AutumnOrange_Secondary,
    background = AutumnOrange_Background,
    onBackground = AutumnOrange_OnBackground
)

@Composable
fun MyCaloriesAppTheme(
    selectedTheme: AppTheme = AppTheme.SAGE,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        AppTheme.SAGE -> if (useDarkTheme) SageDarkColorScheme else SageLightColorScheme
        AppTheme.PASTEL_BLUE -> PastelBlueColorScheme
        AppTheme.PASTEL_PINK -> PastelPinkColorScheme
        AppTheme.PASTEL_ORANGE -> PastelOrangeColorScheme
        AppTheme.PURPLE -> PurpleColorScheme
        AppTheme.NAVY -> NavyColorScheme
        AppTheme.AUTUMN_ORANGE -> AutumnOrangeColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}