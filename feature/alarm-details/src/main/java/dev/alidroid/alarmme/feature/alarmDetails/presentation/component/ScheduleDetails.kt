package dev.alidroid.alarmme.feature.alarmDetails.presentation.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.feature.alarmDetails.R
import dev.alidroid.alarmme.ui.component.AmDaysRepeatPicker
import dev.alidroid.alarmme.ui.component.AmDisclosureRow
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconDefaults
import dev.alidroid.alarmme.ui.component.AmSectionCard
import dev.alidroid.alarmme.ui.component.AmSwitchToggle
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
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
                text = stringResource(R.string.schedule_details_title),
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.schedule_details_custom_date),
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
