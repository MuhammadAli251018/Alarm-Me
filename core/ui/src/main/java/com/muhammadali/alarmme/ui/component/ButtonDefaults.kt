package com.muhammadali.alarmme.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

internal const val DisabledContainerAlpha = 0.12f
internal const val DisabledContentAlpha = 0.38f
internal val PressedShadowColor = Color.Black.copy(alpha = 0.15f)

internal data class AmButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val outlineColor: Color
) {
    companion object
}

@Composable
internal fun AmButtonColors.Companion.colors(
    containerColor: Color,
    contentColor: Color,
    outlineColor: Color,
    disabledContainerColor: Color = containerColor.copy(alpha = DisabledContainerAlpha),
    disabledContentColor: Color = contentColor.copy(alpha = DisabledContentAlpha)
): AmButtonColors {
    return AmButtonColors(
        containerColor,
        contentColor,
        disabledContainerColor,
        disabledContentColor,
        outlineColor
    )
}

@Composable
internal fun AmButtonColors.Companion.solid(
    containerColor: Color,
    contentColor: Color
): AmButtonColors {
    val onSurface = LocalAlarmMeColors.current.onSurface
    return AmButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = onSurface.copy(alpha = DisabledContainerAlpha),
        disabledContentColor = onSurface.copy(alpha = DisabledContentAlpha),
        outlineColor = Color.Transparent
    )
}

@Composable
internal fun AmButtonColors.Companion.outlined(
    outlineColor: Color,
    contentColor: Color = LocalAlarmMeColors.current.onSurface,
    containerColor: Color = LocalAlarmMeColors.current.surface
): AmButtonColors {
    val onSurface = LocalAlarmMeColors.current.onSurface
    return AmButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = onSurface.copy(alpha = DisabledContainerAlpha),
        disabledContentColor = onSurface.copy(alpha = DisabledContentAlpha),
        outlineColor = outlineColor
    )
}
