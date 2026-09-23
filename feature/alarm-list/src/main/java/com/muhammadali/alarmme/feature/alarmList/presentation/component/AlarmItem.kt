package com.muhammadali.alarmme.feature.alarmList.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.alarmList.R
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmItemState
import com.muhammadali.alarmme.ui.component.AmCheckBox
import com.muhammadali.alarmme.ui.component.AmSectionCard
import com.muhammadali.alarmme.ui.component.AmSwitchToggle
import com.muhammadali.alarmme.ui.component.AmBadge
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalTime

internal const val ALARM_ITEM_HEIGHT = 90
internal const val ALARM_ITEM_MIN_WIDTH = 280
internal const val ALARM_ITEM_ITEMS_PADDINGS = 13
internal const val ALARM_ITEM_CONTENT_HORIZONTAL_PADDING = 16
internal const val ALARM_ITEM_CONTENT_VERTICAL_PADDING = 8
internal const val STATUS_DOT_SIZE = 8
private const val SELECTED_RING_WIDTH = 2
private const val OFF_HAIRLINE_WIDTH = 1
private const val TIME_MERIDIEM_GAP = 6

@Composable
fun AlarmItem(
    modifier: Modifier,
    alarmItem: AlarmItemState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onSelectedChange: (Boolean) -> Unit,
    selectionMode: Boolean = false,
    selected: Boolean = false,
) {
    AlarmItem(
        modifier = modifier,
        enabled = alarmItem.enabled,
        time = alarmItem.time,
        description = alarmItem.description,
        onClick = onClick,
        onLongClick = onLongClick,
        onCheckedChange = onCheckedChange,
        selectionMode = selectionMode,
        selected = selected,
        onSelectedChange = onSelectedChange
    )
}

@Composable
fun AlarmItem(
    modifier: Modifier,
    enabled: Boolean,
    time: LocalTime,
    description: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    selectionMode: Boolean = false,
    selected: Boolean = false,
    onSelectedChange: (Boolean) -> Unit = {}
) {
    val colors = LocalAlarmMeColors.current
    val shape = LocalAlarmMeShapes.current.large

    val containerColor = when {
        selected -> colors.surfaceContainerHigh
        enabled -> colors.surfaceContainer
        else -> colors.surface
    }
    val borderWidth = when {
        selected -> SELECTED_RING_WIDTH.dp
        enabled -> 0.dp
        else -> OFF_HAIRLINE_WIDTH.dp
    }
    val borderColor = when {
        selected -> colors.primary
        enabled -> Color.Transparent
        else -> colors.outline.copy(alpha = 0.5f)
    }


    AmSectionCard(
        modifier = modifier
            .height(ALARM_ITEM_HEIGHT.dp)
            .sizeIn(minWidth = ALARM_ITEM_MIN_WIDTH.dp)
            .border(borderWidth, borderColor, shape),
        shape = shape,
        containerColor = containerColor,
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    horizontal = ALARM_ITEM_CONTENT_HORIZONTAL_PADDING.dp,
                    vertical = ALARM_ITEM_CONTENT_VERTICAL_PADDING.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ALARM_ITEM_ITEMS_PADDINGS.dp)
        ) {
            if (selectionMode) {
                AmCheckBox(
                    checked = selected,
                    onCheckedChange = onSelectedChange
                )
            } else {
                StatusDot(if (enabled) colors.primary else colors.outline)
            }

            Column(modifier = Modifier.weight(1f)) {
                AlarmItemTime(time, enabled)

                Text(
                    text = description,
                    style = LocalAlarmMeTypography.current.bodySmall,
                    color = if (enabled) colors.onSurfaceVariant else colors.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!selectionMode) {
                AmSwitchToggle(enabled, onCheckedChange)
            }
        }
    }
}

@Composable
fun AlarmItemTime(time: LocalTime, enabled: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(TIME_MERIDIEM_GAP.dp)
    ) {
        AlarmTimeText(time, enabled)

        if (!enabled) {
            AmBadge(
                modifier = Modifier.padding(bottom = TIME_MERIDIEM_GAP.dp),
                text = stringResource(R.string.alarm_item_off_badge),
                enabled = false
            )
        }
    }
}

@Composable
fun StatusDot(color: Color, modifier: Modifier = Modifier) {
    Box(modifier
        .clip(LocalAlarmMeShapes.current.full)
        .size(STATUS_DOT_SIZE.dp)
        .background(color)
    )
}

@Composable
fun AlarmTimeText(time: LocalTime, enabled: Boolean) {
    val colors = LocalAlarmMeColors.current
    val timeString = remember(time) { time.toFormattedString() }
    val meridiem = remember(time) { if (time.hour < 12) "AM" else "PM" }

    Text(
        text = timeString,
        style = LocalAlarmMeTypography.current.alarmRow,
        color = if (enabled) colors.onSurface else colors.outline,
        textDecoration = if (enabled) TextDecoration.None else TextDecoration.LineThrough
    )

    Text(
        modifier = Modifier.padding(bottom = TIME_MERIDIEM_GAP.dp),
        text = meridiem,
        style = LocalAlarmMeTypography.current.meridiem,
        color = if (enabled) colors.onSurfaceVariant else colors.outline
    )
}

private fun LocalTime.toFormattedString(): String {
    val hourInTwelves = if (hour % 12 == 0) 12 else hour % 12
    return "$hourInTwelves:${minute.toString().padStart(2, '0')}"
}

@Preview(name = "Light Mode--Default Enabled", showBackground = true)
@Preview(
    name = "Dark Mode--Default Enabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemDefaultEnabledPreview() {
    var isEnabled by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = isEnabled,
            time = LocalTime(6, 40),
            description = "Mon–Fri · Weekday mornings, gently",
            onClick = {},
            onLongClick = {},
            onCheckedChange = { isEnabled = it }
        )
    }
}

@Preview(name = "Light Mode--Default Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Default Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemDefaultDisabledPreview() {
    var isEnabled by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = isEnabled,
            time = LocalTime(13, 5),
            description = "Thu 10 Sep · Take the bread out",
            onClick = {},
            onLongClick = {},
            onCheckedChange = { isEnabled = it }
        )
    }
}

@Preview(name = "Light Mode--Selection Unselected Enabled", showBackground = true)
@Preview(
    name = "Dark Mode--Selection Unselected Enabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemSelectionUnselectedEnabledPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = true,
            time = LocalTime(7, 15),
            description = "Sat, Sun",
            onClick = {},
            onLongClick = {},
            onCheckedChange = {},
            selectionMode = true,
            selected = isSelected,
            onSelectedChange = { isSelected = it }
        )
    }
}

@Preview(name = "Light Mode--Selection Unselected Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Selection Unselected Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemSelectionUnselectedDisabledPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = false,
            time = LocalTime(21, 45),
            description = "Every day · Wind down",
            onClick = {},
            onLongClick = {},
            onCheckedChange = {},
            selectionMode = true,
            selected = isSelected,
            onSelectedChange = { isSelected = it }
        )
    }
}

@Preview(name = "Light Mode--Selection Selected Enabled", showBackground = true)
@Preview(
    name = "Dark Mode--Selection Selected Enabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemSelectionSelectedEnabledPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = true,
            time = LocalTime(5, 30),
            description = "Mon, Wed, Fri · Gym — leave by six",
            onClick = {},
            onLongClick = {},
            onCheckedChange = {},
            selectionMode = true,
            selected = isSelected,
            onSelectedChange = { isSelected = it }
        )
    }
}

@Preview(name = "Light Mode--Selection Selected Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Selection Selected Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemSelectionSelectedDisabledPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AlarmItem(
            modifier = Modifier.padding(10.dp).width(350.dp),
            enabled = false,
            time = LocalTime(22, 30),
            description = "Every day · Kettle off",
            onClick = {},
            onLongClick = {},
            onCheckedChange = {},
            selectionMode = true,
            selected = isSelected,
            onSelectedChange = { isSelected = it }
        )
    }
}
