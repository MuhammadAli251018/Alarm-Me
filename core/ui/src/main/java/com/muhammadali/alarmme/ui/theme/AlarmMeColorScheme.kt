package com.muhammadali.alarmme.ui.theme

import androidx.compose.ui.graphics.Color

class AlarmMeColorScheme(
    // Primary
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    // Secondary
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    // Tertiary
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    // Error
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    // Background
    val background: Color,
    val onBackground: Color,

    // Surface
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,

    val outline: Color,
    val outlineVariant: Color,

    val scrim: Color,
    val inverseSurface: Color
)

val LightColorScheme = AlarmMeColorScheme(
    // Primary
    primary = Terracotta47,                     // Armed alarm, FAB, key actions
    onPrimary = Terracotta98,                   // Text/icons on primary
    primaryContainer = Terracotta90,            // Snooze, selected chips
    onPrimaryContainer = Terracotta13,          // Content in containers

    // Secondary
    secondary = Kraft40,                        // Kraft — meta, subtitles
    onSecondary = Kraft98,
    secondaryContainer = Kraft88,               // One-off date badge
    onSecondaryContainer = Kraft13,

    // Tertiary
    tertiary = Sage42,                          // Sage — confirmations, "saved"
    onTertiary = Kraft98,
    tertiaryContainer = Sage90,
    onTertiaryContainer = Sage13,

    // Error
    error = Ember40,                            // Destructive only — never for "armed"
    onError = Ember98,
    errorContainer = Ember89,
    onErrorContainer = Ember11,

    // Background
    background = Ash98,                         // App canvas
    onBackground = Ash11,                       // Warm-shifted white, never #FFF

    // Surface
    surface = Ash98,
    onSurface = Ash11,                          // Time readouts
    surfaceVariant = Kraft90,                   // Off toggle track, wells
    onSurfaceVariant = Kraft35,                 // Repeat subtitle, AM/PM
    surfaceContainerLowest = Ash99,             // Ringing screen base
    surfaceContainerLow = Ash96,                // Bottom sheet
    surfaceContainer = Ash94,                   // Alarm row card
    surfaceContainerHigh = Ash92,               // Dialog, list item inside sheet
    surfaceContainerHighest = Kraft89,          // Snackbar, pressed row

    // Outline
    outline = Kraft49,                          // Outlined button, off thumb
    outlineVariant = Kraft83,                   // Hairlines, OFF chip border

    // Inverse & scrim
    scrim = Ash11,                              // 62% alpha behind sheets
    inverseSurface = Ash19,                     // Reserved; snackbars stay warm
)

val DarkColorScheme = AlarmMeColorScheme(
    // Primary
    primary = Terracotta70,
    onPrimary = Terracotta14,
    primaryContainer = Terracotta38,
    onPrimaryContainer = Terracotta91,

    // Secondary
    secondary = Kraft80,
    onSecondary = Kraft20,
    secondaryContainer = Kraft29,
    onSecondaryContainer = Kraft91,

    // Tertiary
    tertiary = Sage73,
    onTertiary = Sage19,
    tertiaryContainer = Sage30,
    onTertiaryContainer = Sage90,

    // Error
    error = Ember78,
    onError = Ember20,
    errorContainer = Ember30,
    onErrorContainer = Ember90,

    // Background
    background = Ash6,
    onBackground = Ash93,

    // Surface
    surface = Ash9,
    onSurface = Ash93,
    surfaceVariant = Ash21,
    onSurfaceVariant = Kraft76,
    surfaceContainerLowest = Ash4,
    surfaceContainerLow = Ash10,
    surfaceContainer = Ash12,
    surfaceContainerHigh = Ash15,
    surfaceContainerHighest = Ash19,

    // Outline
    outline = Kraft54,
    outlineVariant = Ash28,

    // Inverse & scrim
    scrim = Ash3,
    inverseSurface = Kraft89,
)