package com.muhammadali.alarmme.ui.screen.timePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIfElse

private const val DisabledFieldAlpha = 0.4f
private const val MinHour = 1
private const val MaxHour = 12
private const val MinMinute = 0
private const val MaxMinute = 59
private val FieldGap = 8.dp
private val PillGap = 8.dp
private val PillHorizontalPadding = 16.dp
private val PillVerticalPadding = 8.dp

@Composable
fun AmTimePicker(
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val hourFocusRequester = remember { FocusRequester() }
    val minuteFocusRequester = remember { FocusRequester() }
    val hourState = rememberDigitFieldState(value = hour, minValue = MinHour, maxValue = MaxHour)
    val minuteState =
        rememberDigitFieldState(value = minute, minValue = MinMinute, maxValue = MaxMinute)

    Row(
        modifier = modifier
            .alpha(if (enabled) 1f else DisabledFieldAlpha)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FieldGap)
    ) {
        DigitField(
            state = hourState,
            value = hour,
            label = "HOUR",
            enabled = enabled,
            imeAction = ImeAction.Next,
            focusRequester = hourFocusRequester,
            onDigitsComplete = { minuteFocusRequester.requestFocus() },
            onValueChange = onHourChange
        )

        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ":",
                style = typography.alarmWheelNeighbor,
                color = colors.onSurfaceVariant
            )
        }

        DigitField(
            state = minuteState,
            value = minute,
            label = "MINUTE",
            enabled = enabled,
            imeAction = ImeAction.Done,
            focusRequester = minuteFocusRequester,
            onDeleteAtStart = { hourFocusRequester.requestFocus() },
            onValueChange = onMinuteChange
        )

        Column(
            modifier = Modifier.padding(start = FieldGap / 2),
            verticalArrangement = Arrangement.spacedBy(PillGap)
        ) {
            MeridiemPill(
                text = "AM",
                selected = isAm,
                enabled = enabled,
                onClick = { if (!isAm) onAmPmChange(true) }
            )

            MeridiemPill(
                text = "PM",
                selected = !isAm,
                enabled = enabled,
                onClick = { if (isAm) onAmPmChange(false) }
            )
        }
    }
}

@Composable
private fun MeridiemPill(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    Box(
        modifier = modifier
            .clip(shapes.medium)
            .modifyIfElse(
                condition = selected,
                thenModifier = { background(colors.primaryContainer) },
                otherwiseModifier = { border(1.dp, colors.outline, shapes.medium) }
            )
            .toggleable(
                value = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onValueChange = { onClick() }
            )
            .padding(horizontal = PillHorizontalPadding, vertical = PillVerticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.meridiem,
            color = if (selected) colors.onPrimaryContainer else colors.onSurfaceVariant
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmTimePickerPreview() {
    AlarmMeThemeProvider {
        var hour by remember { mutableStateOf(10) }
        var minute by remember { mutableStateOf(30) }
        var isAm by remember { mutableStateOf(true) }

        AmTimePicker(
            modifier = Modifier.padding(16.dp),
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = { hour = it },
            onMinuteChange = { minute = it },
            onAmPmChange = { isAm = it }
        )

    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmTimePickerNoonPreview() {
    AlarmMeThemeProvider {
        var hour by remember { mutableStateOf(12) }
        var minute by remember { mutableStateOf(0) }
        var isAm by remember { mutableStateOf(false) }

        AmTimePicker(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = { hour = it },
            onMinuteChange = { minute = it },
            onAmPmChange = { isAm = it }
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmTimePickerDisabledPreview() {
    AlarmMeThemeProvider {
        AmTimePicker(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            hour = 7,
            minute = 5,
            isAm = true,
            enabled = false,
            onHourChange = {},
            onMinuteChange = {},
            onAmPmChange = {}
        )
    }
}
