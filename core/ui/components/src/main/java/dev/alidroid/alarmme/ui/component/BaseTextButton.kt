package dev.alidroid.alarmme.ui.component

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

/** The metrics every text button variant shares, exposed so placeholders can reserve the same box. */
object AmTextButtonDefaults {
    val Height: Dp = 48.dp
    val MinWidth: Dp = 88.dp
    val ContentHorizontalPadding: Dp = 24.dp
}

/**
 * Shape, size and interaction behaviour shared by every text button variant.
 *
 * It resolves no colors of its own: [colors] arrives fully resolved (active + disabled pair), so
 * the calling variant owns constructing it. [enabled] picks which half of [colors] is drawn and
 * gates the click — it never resolves a color itself.
 *
 * Decoration drawn *outside* the container — selection outline, border — belongs to the variant
 * and should be applied to the [modifier] it passes down.
 */
@Composable
internal fun BaseTextButton(
    text: String,
    colors: AmButtonColors,
    modifier: Modifier = Modifier,
    shape: Shape = LocalAlarmMeShapes.current.full,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val pressedState = interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .height(AmTextButtonDefaults.Height)
            .sizeIn(minWidth = AmTextButtonDefaults.MinWidth)
            .clip(shape = shape)
            .background(containerColor)
            .dropShadow(shape) {
                color = if (pressedState.value) PressedShadowColor else Color.Transparent
            }
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
            colors = AmButtonColors.solid(colors.secondaryContainer, colors.onSecondaryContainer),
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
        BaseTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Shadow",
            colors = AmButtonColors.solid(colors.secondaryContainer, colors.onSecondaryContainer),
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        )
    }
}
