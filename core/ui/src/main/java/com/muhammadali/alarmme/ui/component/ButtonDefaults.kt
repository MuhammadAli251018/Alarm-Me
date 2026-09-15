package com.muhammadali.alarmme.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

internal const val DisabledContainerAlpha = 0.12f
internal const val DisabledContentAlpha = 0.38f
internal val PressedShadowColor = Color.Black.copy(alpha = 0.15f)

/**
 * The active/disabled container+content pair every button-shaped [BaseTextButton], [BaseFabButton]
 * and [BaseIconButton] variant needs. Both states travel together so the base composable can pick
 * between them off [enabled] without the caller resolving disabled alpha itself.
 */
internal data class AmButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
) {
    companion object
}

/** A filled look: colored container, disabled state dims toward [LocalAlarmMeColors.onSurface]. */
@Composable
internal fun AmButtonColors.Companion.solid(containerColor: Color, contentColor: Color): AmButtonColors {
    val onSurface = LocalAlarmMeColors.current.onSurface
    return AmButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = onSurface.copy(alpha = DisabledContainerAlpha),
        disabledContentColor = onSurface.copy(alpha = DisabledContentAlpha)
    )
}

/** A transparent-container look: only the content color carries meaning. */
@Composable
internal fun AmButtonColors.Companion.outlined(contentColor: Color): AmButtonColors {
    val onSurface = LocalAlarmMeColors.current.onSurface
    return AmButtonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        disabledContainerColor = onSurface.copy(alpha = DisabledContainerAlpha),
        disabledContentColor = onSurface.copy(alpha = DisabledContentAlpha)
    )
}
