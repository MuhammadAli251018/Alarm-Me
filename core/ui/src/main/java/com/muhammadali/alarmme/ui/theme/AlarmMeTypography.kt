package com.muhammadali.alarmme.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.R

val WorkSans = FontFamily(
    Font(R.font.work_sans_light, FontWeight.Light),
    Font(R.font.work_sans_regular, FontWeight.Normal),
    Font(R.font.work_sans_medium, FontWeight.Medium),
    Font(R.font.work_sans_semibold, FontWeight.SemiBold),
)

val PlexMono = FontFamily(
    Font(R.font.ibm_plex_mono_extralight, FontWeight.ExtraLight),
    Font(R.font.ibm_plex_mono_light, FontWeight.Light),
    Font(R.font.ibm_plex_mono_medium, FontWeight.Medium),
)

@Immutable
data class AlarmMeTypography(
    // Display
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,

    // Headline
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,

    // Title
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,

    // Body
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,

    // Label
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,

    // Clock
    val alarmDisplay: TextStyle,
    val alarmRow: TextStyle,
    val alarmWheelSelected: TextStyle,
    val alarmWheelNeighbor: TextStyle,
    val meridiem: TextStyle,
)

val DefaultTypography = AlarmMeTypography(
    // Display
    displayLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Light,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Headline
    headlineLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // Title
    titleLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.1).sp,
    ),
    titleMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Body
    bodyLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),

    // Label
    labelLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.4.sp,
    ),

    // Clock
    alarmDisplay = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.Medium,
        fontSize = 76.sp,
        lineHeight = 80.sp,
        letterSpacing = (-2.0).sp
    ),
    alarmRow = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.Light,
        fontSize = 38.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.8).sp,
    ),
    alarmWheelSelected = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 58.sp,
        lineHeight = TextUnit.Unspecified,
    ),
    alarmWheelNeighbor = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.Light,
        fontSize = 34.sp,
        lineHeight = TextUnit.Unspecified,
    ),
    meridiem = TextStyle(
        fontFamily = PlexMono,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
    )
)