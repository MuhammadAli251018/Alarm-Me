package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

private val TrackWidth = 52.dp
private val TrackHeight = 32.dp
private val TrackPadding = 2.dp
private val TrackBorderWidth = 1.dp
private val ThumbSize = 26.dp
private const val ToggleDurationMillis = 300
private const val LabelFadeMillis = 150

@Composable
fun AmSwitchToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = LocalAlarmMeColors.current
    val shape = LocalAlarmMeShapes.current.full

    val trackFillColor by animateColorAsState(
        targetValue = when {
            !checked -> Color.Transparent
            !enabled -> colors.outlineVariant.copy(alpha = 0.55f)
            else -> colors.primary
        },
        animationSpec = tween(ToggleDurationMillis),
        label = "trackFillColor"
    )
    val trackOutlineColor by animateColorAsState(
        targetValue = when {
            checked -> Color.Transparent
            enabled -> colors.onSurfaceVariant
            else -> colors.outlineVariant
        },
        animationSpec = tween(ToggleDurationMillis),
        label = "trackOutlineColor"
    )
    val thumbColor by animateColorAsState(
        targetValue = when {
            checked && enabled -> colors.onPrimary
            !checked && enabled -> colors.onSurfaceVariant
            checked && !enabled -> colors.surface.copy(alpha = 0.55f)
            else -> colors.outlineVariant
        },
        animationSpec = tween(ToggleDurationMillis),
        label = "thumbColor"
    )

    BoxWithConstraints(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(TrackWidth, TrackHeight)
            .clip(shape)
            .background(trackFillColor)
            .border(TrackBorderWidth, trackOutlineColor, shape)
            .toggleable(
                value = checked,
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(horizontal = TrackPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        val thumbOffset by animateDpAsState(
            targetValue = if (checked) maxWidth - ThumbSize else 0.dp,
            animationSpec = tween(ToggleDurationMillis),
            label = "thumbOffset"
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = thumbOffset.toPx()
                }
                .size(ThumbSize)
                .clip(shape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = checked && enabled,
                enter = fadeIn(tween(LabelFadeMillis)),
                exit = fadeOut(tween(LabelFadeMillis))
            ) {
                Text(
                    text = "ON",
                    style = LocalAlarmMeTypography.current
                        .labelSmall.copy(letterSpacing = 0.1.sp),
                    color = colors.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(name = "Light Mode--Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSwitchToggleUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmSwitchToggle(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSwitchToggleCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmSwitchToggle(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Disabled Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSwitchToggleDisabledUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmSwitchToggle(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            enabled = false,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Disabled Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSwitchToggleDisabledCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmSwitchToggle(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            enabled = false,
            onCheckedChange = { isChecked = it }
        )
    }
}
