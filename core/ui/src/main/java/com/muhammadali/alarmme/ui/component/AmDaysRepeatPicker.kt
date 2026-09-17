package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.ScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isLandscapePhone
import com.muhammadali.alarmme.ui.screenCategory.isPortraitPhone
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIfElse
import kotlinx.datetime.DayOfWeek

private const val DisabledChipAlpha = 0.4f

@Composable
fun AmDaysRepeatPicker(
    modifier: Modifier = Modifier,
    selectedDays: Set<DayOfWeek>,
    enabled: Boolean = true,
    onDayToggle: (DayOfWeek) -> Unit,
) {
    val screenCategory = currentScreenCategory()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else DisabledChipAlpha),
        horizontalArrangement = Arrangement.spacedBy(chipGap(screenCategory))
    ) {
        DayOfWeek.entries.forEach { day ->
            RepeatDayChip(
                modifier = Modifier.modifyIfElse(
                    condition = screenCategory.isPortraitPhone,
                    thenModifier = { width(44.dp) },
                    otherwiseModifier = { weight(1f) }
                ),
                day = day,
                selected = selectedDays.contains(day),
                enabled = enabled,
                height = chipHeight(screenCategory),
                onToggle = { onDayToggle(day) }
            )
        }
    }
}

private fun chipHeight(screenCategory: ScreenCategory): Dp = when {
    screenCategory.isLandscapePhone -> 44.dp
    screenCategory.isExpandedWidthOrWider -> 52.dp
    else -> 48.dp
}

private fun chipGap(screenCategory: ScreenCategory): Dp =
    if (screenCategory.isLandscapePhone) 6.dp else 8.dp

@Composable
private fun RepeatDayChip(
    day: DayOfWeek,
    selected: Boolean,
    enabled: Boolean,
    height: Dp,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    Column(
        modifier = modifier
            .height(height)
            .clip(shapes.medium)
            .modifyIfElse(
                condition = selected,
                thenModifier = {
                    background(colors.primary)
                },
                otherwiseModifier = {
                    border(1.dp, colors.outlineVariant, shapes.medium)
                }
            )
            .toggleable(
                value = selected,
                enabled = enabled,
                onValueChange = { onToggle() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
    ) {
        val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

        Text(
            text = day.name.first().toString(),
            style = typography.titleSmall,
            color = contentColor
        )

        if (selected) {
            Box(Modifier.size(3.dp).clip(shapes.full).background(contentColor))
        }
    }
}

private fun previewSelectedDays(): Set<DayOfWeek> =
    DayOfWeek.entries.subList(0, 4).toSet()

@CommonScreenCategoriesPreview
@Composable
private fun AmDaysRepeatPickerPreview() {
    AlarmMeThemeProvider {
        AmDaysRepeatPicker(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            selectedDays = previewSelectedDays(),
            onDayToggle = {}
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmDaysRepeatPickerDisabledPreview() {
    AlarmMeThemeProvider {
        AmDaysRepeatPicker(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            selectedDays = previewSelectedDays(),
            enabled = false,
            onDayToggle = {}
        )
    }
}
