package dev.alidroid.alarmme.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAlarmMeColors = staticCompositionLocalOf<AlarmMeColorScheme> {
    error("No AlarmMeColors provided")
}

val LocalAlarmMeTypography = staticCompositionLocalOf<AlarmMeTypography> {
    error("No DefaultTypography provided")
}

val LocalAlarmMeShapes = staticCompositionLocalOf<AlarmMeShapes> {
    error("No AlarmMeShapes provided")
}

val LocalAlarmMeDimensions = staticCompositionLocalOf<AlarmMeDimensions> {
    error("No DefaultDimensions provided")
}


object AlarmMeTheme {
    val colors: AlarmMeColorScheme
        @Composable
        get() = LocalAlarmMeColors.current

    val typography: AlarmMeTypography
        @Composable
        get() = LocalAlarmMeTypography.current

    val shapes: AlarmMeShapes
        @Composable
        get() = LocalAlarmMeShapes.current

    val dimensions: AlarmMeDimensions
        @Composable
        get() = LocalAlarmMeDimensions.current
}

@Composable
fun AlarmMeThemeProvider(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalAlarmMeColors provides colorScheme,
        LocalAlarmMeTypography provides DefaultTypography,
        LocalAlarmMeShapes provides DefaultShapes,
        LocalAlarmMeDimensions provides DefaultDimensions,
        content = content
    )
}