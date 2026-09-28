package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.ScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography


private val DefaultBorderWidth = 1.dp
private val PressedBorderWidth = 2.dp

@Composable
fun AmQuickActionButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    /** Scales min width according to screen category.*/
    scaleWidthWithScreen: Boolean = true,
    onClick: () -> Unit
) {

    val screenCategory = currentScreenCategory()
    val shape = LocalAlarmMeShapes.current.small
    val buttonColors = AmButtonColors.outlined(LocalAlarmMeColors.current.outline)
    val borderColor = if (enabled) buttonColors.outlineColor else buttonColors.disabledContentColor
    val pressedState = interactionSource.collectIsPressedAsState()
    val borderWidth by animateDpAsState(
        targetValue = if (pressedState.value) PressedBorderWidth else DefaultBorderWidth,
        animationSpec = tween(durationMillis = 150),
        label = "borderWidth"
    )

    val containerColor = if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor
    val contentColor = if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor
    val minWidth = if (scaleWidthWithScreen) getMinWidth(screenCategory) else Dp.Unspecified

    Box(
        modifier = modifier
            .widthIn(min = minWidth)
            .heightIn(min = 48.dp)
            .height(AmTextButtonDefaults.Height)
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
        Text(
            modifier = Modifier.padding(horizontal = getHorizontalPaddings(screenCategory)),
            text = text,
            style = LocalAlarmMeTypography.current.labelLarge,
            color = contentColor,
        )
    }
}

private fun getMinWidth(screenCategory: ScreenCategory): Dp {
    return when {
        screenCategory.width <= WidthCategory.Medium -> 36.dp
        else -> 48.dp
    }
}

private fun getHorizontalPaddings(screenCategory: ScreenCategory): Dp {
    return when {
        screenCategory.width <= WidthCategory.Medium -> 10.dp
        else -> 20.dp
    }
}



@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmQuickActionButtonPreview() {
    AlarmMeThemeProvider {
        AmQuickActionButton(
            modifier = Modifier.padding(10.dp),
            text = "10s",
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmQuickActionButtonClickedPreview() {
    val interactionSource = rememberPressedInteractionSource()

    AlarmMeThemeProvider {
        AmQuickActionButton(
            modifier = Modifier.padding(10.dp),
            text = "10s",
            interactionSource = interactionSource,
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmQuickActionButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmQuickActionButton(
            modifier = Modifier.padding(10.dp),
            text = "10s",
            enabled = false,
            onClick = {}
        )
    }
}
