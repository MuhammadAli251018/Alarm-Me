package com.muhammadali.alarmme.feature.alarmDetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.AlarmSettingsDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.AlarmTimeDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.ScheduleDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.ScreenDetailsTopBar
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isLandscapePhone
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class AlarmDetailsScreen : Screen {

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }
}

/** Left-pane width in [AlarmDetailsHorizontalContent], keyed off the same breakpoint the design uses. */
private val LandscapePhoneLeftPaneWidth = 352.dp
private val ExpandedLeftPaneWidth = 480.dp

/** The single-column layout's content, capped/centered from medium width up, per the design. */
private val VerticalContentMaxWidth = 480.dp

@Composable
fun AlarmDetailsScreenContent(
    modifier: Modifier = Modifier,
    alarmTitle: String,
    canSave: Boolean,
    time: LocalDateTime,
    repeatDays: Set<DayOfWeek>,
    customDateEnabled: Boolean,
    customDateLabel: String,
    soundLabel: String,
    snoozeLabel: String,
    gradualVolumeEnabled: Boolean,
    vibrateEnabled: Boolean,
    alarmLabel: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val screenCategory = currentScreenCategory()

    Column(modifier = modifier.fillMaxSize()) {
        ScreenDetailsTopBar(
            modifier = Modifier.fillMaxWidth(),
            alarmTitle = alarmTitle,
            canSave = canSave,
            onBackClick = onBackClick,
            onSaveButtonClick = onSaveClick,
            onDeleteButtonClick = onDeleteClick
        )

        if (screenCategory.isLandscapePhone || screenCategory.isExpandedWidthOrWider) {
            AlarmDetailsHorizontalContent(
                modifier = Modifier.fillMaxSize(),
                time = time,
                repeatDays = repeatDays,
                customDateEnabled = customDateEnabled,
                customDateLabel = customDateLabel,
                soundLabel = soundLabel,
                snoozeLabel = snoozeLabel,
                gradualVolumeEnabled = gradualVolumeEnabled,
                vibrateEnabled = vibrateEnabled,
                alarmLabel = alarmLabel,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        } else {
            AlarmDetailsVerticalContent(
                modifier = Modifier.fillMaxSize(),
                time = time,
                repeatDays = repeatDays,
                customDateEnabled = customDateEnabled,
                customDateLabel = customDateLabel,
                soundLabel = soundLabel,
                snoozeLabel = snoozeLabel,
                gradualVolumeEnabled = gradualVolumeEnabled,
                vibrateEnabled = vibrateEnabled,
                alarmLabel = alarmLabel,
                onDeleteClick = onDeleteClick,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        }
    }
}

/** Compact-portrait and medium-width: one scrolling column, capped/centered past medium width. */
@Composable
private fun AlarmDetailsVerticalContent(
    modifier: Modifier = Modifier,
    time: LocalDateTime,
    repeatDays: Set<DayOfWeek>,
    customDateEnabled: Boolean,
    customDateLabel: String,
    soundLabel: String,
    snoozeLabel: String,
    gradualVolumeEnabled: Boolean,
    vibrateEnabled: Boolean,
    alarmLabel: String,
    onDeleteClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .widthIn(max = VerticalContentMaxWidth)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        AlarmTimeDetails(modifier = Modifier.fillMaxWidth(), time = time)

        ScheduleDetails(
            modifier = Modifier.fillMaxWidth(),
            repeatDays = repeatDays,
            customDateEnabled = customDateEnabled,
            customDateLabel = customDateLabel,
            onDayToggle = onDayToggle,
            onCustomDateToggle = onCustomDateToggle,
            onCustomDateRowClick = onCustomDateRowClick
        )

        AlarmSettingsDetails(
            modifier = Modifier.fillMaxWidth(),
            soundLabel = soundLabel,
            snoozeLabel = snoozeLabel,
            gradualVolumeEnabled = gradualVolumeEnabled,
            vibrateEnabled = vibrateEnabled,
            alarmLabel = alarmLabel,
            onSoundClick = onSoundClick,
            onSnoozeClick = onSnoozeClick,
            onGradualVolumeToggle = onGradualVolumeToggle,
            onVibrateToggle = onVibrateToggle,
            onLabelClick = onLabelClick
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(onClick = onDeleteClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Delete alarm",
                style = typography.labelLarge,
                color = colors.error
            )
        }
    }
}

/** Landscape-phone and expanded-width: two independently-scrolling panes side by side. */
@Composable
private fun AlarmDetailsHorizontalContent(
    modifier: Modifier = Modifier,
    time: LocalDateTime,
    repeatDays: Set<DayOfWeek>,
    customDateEnabled: Boolean,
    customDateLabel: String,
    soundLabel: String,
    snoozeLabel: String,
    gradualVolumeEnabled: Boolean,
    vibrateEnabled: Boolean,
    alarmLabel: String,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val screenCategory = currentScreenCategory()
    val leftPaneWidth = if (screenCategory.isExpandedWidthOrWider) {
        ExpandedLeftPaneWidth
    } else {
        LandscapePhoneLeftPaneWidth
    }

    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .width(leftPaneWidth)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .drawBehind {
                    drawLine(
                        color = colors.outline.copy(0.5f),
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1f
                    )
                }
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AlarmTimeDetails(modifier = Modifier.fillMaxWidth(), time = time)

            ScheduleDetails(
                modifier = Modifier.fillMaxWidth(),
                repeatDays = repeatDays,
                customDateEnabled = customDateEnabled,
                customDateLabel = customDateLabel,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            AlarmSettingsDetails(
                modifier = Modifier.fillMaxWidth(),
                soundLabel = soundLabel,
                snoozeLabel = snoozeLabel,
                gradualVolumeEnabled = gradualVolumeEnabled,
                vibrateEnabled = vibrateEnabled,
                alarmLabel = alarmLabel,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        }
    }
}

private val PreviewDate = LocalDate(2026, 9, 17)

private fun previewRepeatDays(): Set<DayOfWeek> = setOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY
)

@Composable
private fun PreviewAlarmDetailsScreen(canSave: Boolean = true) {
    AlarmMeThemeProvider {
        AlarmDetailsScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            alarmTitle = "Weekday mornings, gently",
            canSave = canSave,
            time = LocalDateTime(PreviewDate, LocalTime(6, 40)),
            repeatDays = previewRepeatDays(),
            customDateEnabled = false,
            customDateLabel = "Sep 20, 24, 28",
            soundLabel = "Sunrise chime",
            snoozeLabel = "9 min · 3 times",
            gradualVolumeEnabled = true,
            vibrateEnabled = false,
            alarmLabel = "Weekday mornings",
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onDayToggle = {},
            onCustomDateToggle = {},
            onCustomDateRowClick = {},
            onSoundClick = {},
            onSnoozeClick = {},
            onGradualVolumeToggle = {},
            onVibrateToggle = {},
            onLabelClick = {}
        )
    }
}

/**
 * The three common buckets cover phone-portrait (single scrolling column), phone-landscape via
 * compact height (two-pane split), and expanded width (wider two-pane split).
 */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentPreview() {
    PreviewAlarmDetailsScreen()
}
