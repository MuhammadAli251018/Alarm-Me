package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun AmSwitchToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var isChecked by remember { mutableStateOf(checked) }

    val colors = LocalAlarmMeColors.current
    val trackFilledColor = when {
        !isChecked -> Color.Transparent
        !enabled -> colors.outlineVariant.copy(alpha = 0.55f)
        else -> colors.primary
    }
    val trackOutlineColor = when {
        isChecked && enabled -> Color.Transparent
        !isChecked && enabled -> colors.onSurfaceVariant
        isChecked && !enabled -> colors.surface
        else -> colors.outlineVariant
    }

    BoxWithConstraints(
        modifier = modifier
            .size(52.dp, 32.dp)
            .border(1.dp, trackOutlineColor, LocalAlarmMeShapes.current.full)
            .clip(LocalAlarmMeShapes.current.full)
            .background(trackFilledColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(horizontal = 2.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        val thumbColor = when {
            isChecked && enabled -> colors.onPrimary
            !isChecked && enabled -> colors.onSurfaceVariant
            isChecked && !enabled -> colors.surface.copy(alpha = 0.55f)
            else -> colors.outlineVariant
        }

        val animationDistance = with(LocalDensity.current){ (maxWidth - 26.dp).toPx() }
        val switchAnimationProgress = remember { Animatable(if (checked) 0f else 1f) }

        LaunchedEffect(checked) {
            switchAnimationProgress.animateTo(
                targetValue = if (checked) 0f else 1f,
                animationSpec = tween(300)
            )
            isChecked = checked
        }


        Box(
            modifier = Modifier
                .size(26.dp)
                .graphicsLayer {
                    translationX = -switchAnimationProgress.value * animationDistance
                }
                .clip(LocalAlarmMeShapes.current.full)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                isChecked && enabled,
                enter = fadeIn(tween(150)),
                exit = fadeOut(tween(150))
            ) {
                Text(
                    text = "ON",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp),
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
