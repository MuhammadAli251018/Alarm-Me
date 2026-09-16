package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography


object AmTextWithIconButtonDefaults {
    val MinHeight: Dp = 48.dp
    val MinWidth: Dp = 48.dp
    val ContentHorizontalPadding: Dp = 24.dp
}

private val DefaultBorderWidth: Dp = 1.dp
private val PressedBorderWidth: Dp = 2.dp


@Composable
internal fun BaseTextWithIconButton(
    text: String,
    colors: AmButtonColors,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    shape: Shape = LocalAlarmMeShapes.current.medium,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val borderColor = if (enabled) colors.outlineColor else colors.disabledContentColor
    val pressedState = interactionSource.collectIsPressedAsState()
    val borderWidth by animateDpAsState(
        targetValue = if (pressedState.value) PressedBorderWidth else DefaultBorderWidth,
        animationSpec = tween(durationMillis = 150),
        label = "borderWidth"
    )

    Box(
        modifier = modifier
            .sizeIn(
                minWidth = AmTextWithIconButtonDefaults.MinWidth,
                minHeight = AmTextWithIconButtonDefaults.MinHeight
            )
            .border(borderWidth, borderColor, shape)
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
        Row(
            modifier = Modifier.padding(horizontal = AmTextWithIconButtonDefaults.ContentHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            icon()

            Text(
                text = text,
                style = LocalAlarmMeTypography.current.labelLarge,
                color = contentColor,
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseTextWithIconButtonPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseTextWithIconButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            colors = AmButtonColors.outlined(colors.outline),
            icon = { Text(text = "＋", color = colors.onSurface) },
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseTextWithIconButtonPreview1() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseTextWithIconButton(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            text = "Save",
            colors = AmButtonColors.outlined(colors.outline),
            icon = { Text(text = "＋", color = colors.onSurface) },
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
private fun BaseTextWithIconButtonPressedPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseTextWithIconButton(
            modifier = Modifier.padding(10.dp),
            text = "Shadow",
            colors = AmButtonColors.outlined(colors.outline),
            icon = { Text(
                text = "✕",
                color = colors.onSurface
            ) },
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        )
    }
}

