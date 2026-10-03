package dev.alidroid.alarmme.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

data class AlarmMeShapes(
    val extraSmall: RoundedCornerShape,
    val small: RoundedCornerShape,
    val medium: RoundedCornerShape,
    val large: RoundedCornerShape,
    val extraLarge: RoundedCornerShape,
) {
    val full: RoundedCornerShape = RoundedCornerShape(50)
}

val DefaultShapes = AlarmMeShapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)