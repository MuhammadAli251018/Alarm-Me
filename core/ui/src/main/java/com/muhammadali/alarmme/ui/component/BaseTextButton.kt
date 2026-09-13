package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIf

/** The metrics every text button variant shares, exposed so placeholders can reserve the same box. */
object AmTextButtonDefaults {
    val Height: Dp = 48.dp
    val MinWidth: Dp = 88.dp
    val ContentHorizontalPadding: Dp = 24.dp
}

/**
 * Shape, size and interaction behaviour shared by every text button variant.
 *
 * It resolves no colors of its own: [containerColor], [contentColor] and [pressColor] arrive
 * fully resolved, so the calling variant owns the enabled/disabled decision. [enabled] is used
 * only to gate the click, never to pick a color.
 *
 * Decoration drawn *outside* the container — selection outline, border — belongs to the variant
 * and should be applied to the [modifier] it passes down.
 *
 * @param pressColor drawn over the container while pressed; expected to carry its own alpha.
 * When `null` — the default — the button lifts with a pressed drop shadow instead of tinting.
 */
@Composable
fun BaseTextButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val shape = LocalAlarmMeShapes.current.full
    val pressedState = interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .height(AmTextButtonDefaults.Height)
            .sizeIn(minWidth = AmTextButtonDefaults.MinWidth)
            .dropShadow(shape) {
                color = if (pressedState.value) PressedShadowColor else Color.Transparent
            }
            .clip(shape = shape)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = AmTextButtonDefaults.ContentHorizontalPadding),
            text = text,
            style = LocalAlarmMeTypography.current.labelLarge,
            color = contentColor,
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseTextButtonPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            containerColor = colors.secondaryContainer,
            contentColor = colors.onSecondaryContainer,
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(
    name = "Dark Mode--Pressed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseTextButtonPressedPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // no pressColor: pressed shows as a drop shadow
            BaseTextButton(
                text = "Shadow",
                containerColor = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                interactionSource = rememberPressedInteractionSource(),
                onClick = {}
            )
            // explicit pressColor: pressed shows as an overlay tint
            BaseTextButton(
                text = "Overlay",
                containerColor = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                interactionSource = rememberPressedInteractionSource(),
                onClick = {}
            )
        }
    }
}
