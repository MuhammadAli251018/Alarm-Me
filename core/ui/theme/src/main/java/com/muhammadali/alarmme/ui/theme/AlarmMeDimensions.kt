package com.muhammadali.alarmme.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AlarmMeDimensions(
    val xxs: Dp,
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val xxl: Dp,
    val huge: Dp
)

val DefaultDimensions = AlarmMeDimensions(
    xxs = 4.dp,
    xs = 8.dp,
    sm = 12.dp,
    md = 16.dp,
    lg = 20.dp,
    xl = 24.dp,
    xxl = 32.dp,
    huge = 48.dp
)
