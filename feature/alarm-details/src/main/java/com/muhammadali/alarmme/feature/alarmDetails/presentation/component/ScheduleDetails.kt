package com.muhammadali.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.component.AmDaysRepeatPicker
import com.muhammadali.alarmme.ui.component.AmDisclosureRow
import com.muhammadali.alarmme.ui.component.AmIcon
import com.muhammadali.alarmme.ui.component.AmIconDefaults
import com.muhammadali.alarmme.ui.component.AmSectionCard
import com.muhammadali.alarmme.ui.component.AmSwitchToggle
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeIcons
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.DayOfWeek

@Composable
fun ScheduleDetails(
    modifier: Modifier = Modifier,
    repeatDays: Set<DayOfWeek>,
    customDateEnabled: Boolean,
    customDateLabel: String,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val screenCategory = currentScreenCategory()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SCHEDULE",
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Custom date",
                    style = typography.bodyMedium,
                    color = if (customDateEnabled) colors.onSurface else colors.onSurfaceVariant
                )

                AmSwitchToggle(
                    checked = customDateEnabled,
                    onCheckedChange = onCustomDateToggle
                )
            }
        }

        AmDaysRepeatPicker(
            selectedDays = repeatDays,
            enabled = !customDateEnabled,
            onDayToggle = onDayToggle
        )

        if (customDateEnabled) {
            AmSectionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (screenCategory.isExpandedWidthOrWider) 60.dp else 52.dp),
                shape = LocalAlarmMeShapes.current.medium,
                containerColor = colors.surfaceContainer,
                onClick = onCustomDateRowClick
            ) {
                AmDisclosureRow(
                    modifier = Modifier.fillMaxWidth(),
                    text = customDateLabel
                ) {
                    AmIcon(
                        painter = AlarmMeIcons.ChevronRight,
                        contentDescription = null,
                        size = AmIconDefaults.Small,
                        tint = colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun previewRepeatDays(): Set<DayOfWeek> = setOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY
)

@CommonScreenCategoriesPreview
@Composable
private fun ScheduleDetailsRepeatModePreview() {
    AlarmMeThemeProvider {
        ScheduleDetails(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            repeatDays = previewRepeatDays(),
            customDateEnabled = false,
            customDateLabel = "Sep 20, 24, 28",
            onDayToggle = {},
            onCustomDateToggle = {},
            onCustomDateRowClick = {}
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun ScheduleDetailsCustomDatePreview() {
    AlarmMeThemeProvider {
        ScheduleDetails(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            repeatDays = previewRepeatDays(),
            customDateEnabled = true,
            customDateLabel = "Sep 20, 24, 28",
            onDayToggle = {},
            onCustomDateToggle = {},
            onCustomDateRowClick = {}
        )
    }
}
