package com.muhammadali.alarmme.feature.alarmList.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.alarmList.presentation.alarmsListHorizontalPaddings
import com.muhammadali.alarmme.ui.component.AmDestructiveTextButton
import com.muhammadali.alarmme.ui.component.AmFabExtendedButton
import com.muhammadali.alarmme.ui.component.IconButton
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.HeightCategory
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime


/** Shared with [TopBarSkeleton] so the placeholder bar occupies the same box as the real one. */
internal val TopBarContentHorizontalPadding: Dp = 8.dp
internal val TopBarContentVerticalPadding: Dp = 12.dp
internal val TopBarItemsSpacing: Dp = 8.dp

/**
 * The bar's labels. Shared with [TopBarSkeleton], which measures them to size its placeholder
 * blocks, so a placeholder is always exactly as wide as the text it stands in for.
 */
internal const val TOP_BAR_TITLE = "Alarms"
internal const val TOP_BAR_SELECT_LABEL = "Select"
internal const val TOP_BAR_NEW_ALARM_LABEL = "New alarm"
internal const val TOP_BAR_NEW_ALARM_GLYPH = "＋"

@Composable
fun AlarmsListTopBar(
    modifier: Modifier = Modifier,
    selectedAlarmsCount: Int,
    nextAlarmTime: LocalDateTime?,
    onDismissSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectionClick: () -> Unit,
    onNewAlarmClick: () -> Unit,
) {
    if (selectedAlarmsCount > 0) {
        AlarmsListTopBarSelectionMode(
            modifier = modifier,
            selectedAlarmsCount = selectedAlarmsCount,
            onDismiss = onDismissSelection,
            onSelectAll = onSelectAll
        )
        return
    }

    val screenCategory = currentScreenCategory()

    when {
        screenCategory.height == HeightCategory.Compact ->
            DefaultAlarmsListTopBarCompactHeight(
                modifier = modifier.padding(horizontal = alarmsListHorizontalPaddings()),
                nextAlarmTime = nextAlarmTime,
                onNewAlarmClick = onNewAlarmClick,
                onSelectionClick = onSelectionClick
            )

        screenCategory.width <= WidthCategory.Medium ->
            DefaultAlarmsListTopBarCompactAndMediumWidth(
                modifier = modifier.padding(horizontal = alarmsListHorizontalPaddings()),
                nextAlarmTime = nextAlarmTime,
                onSelectionClick = onSelectionClick
            )

        else ->
            DefaultAlarmsListTopExpandedAndBiggerWidth(
                modifier = modifier.padding(horizontal = alarmsListHorizontalPaddings()),
                nextAlarmTime = nextAlarmTime,
                onNewAlarmClick = onNewAlarmClick,
                onSelectionClick = onSelectionClick
            )
    }
}

@Composable
fun AlarmsListTopBarSelectionMode(
    modifier: Modifier,
    selectedAlarmsCount: Int,
    onDismiss: () -> Unit,
    onSelectAll: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colors.surfaceContainerHigh)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
    ) {
        IconButton(
            modifier = Modifier,
            onClick = onDismiss,
            containerColor = colors.surfaceContainerHigh.copy(alpha = 0.5f),
            contentColor = colors.onSurface,
            glyphStyle = LocalAlarmMeTypography.current.bodyLarge,
        ) {
            Text("✕")
        }

        Text(
            text = "$selectedAlarmsCount selected",
            style = LocalAlarmMeTypography.current.titleMedium,
            color = colors.onSurfaceVariant
        )

        Box(modifier = Modifier.weight(1f)) {
           AmDestructiveTextButton(
               modifier = Modifier.align(Alignment.CenterEnd),
               text = "Select all",
               textColor = colors.onSurface,
               onClick = onSelectAll
           )
        }
    }
}

@Composable
fun DefaultAlarmsListTopBarCompactAndMediumWidth(
    modifier: Modifier = Modifier,
    nextAlarmTime: LocalDateTime?,
    onSelectionClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TopBarContentHorizontalPadding,
                vertical = TopBarContentVerticalPadding
            )
    ) {
        val topBarEyebrow = remember(nextAlarmTime) { nextAlarmTime.toTopBarEyebrow() }
        Text(
            text = topBarEyebrow,
            style = LocalAlarmMeTypography.current.labelSmall,
            color = colors.primary
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = TOP_BAR_TITLE,
                style = LocalAlarmMeTypography.current.headlineSmall,
                color = colors.onSurface,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            AmDestructiveTextButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = TOP_BAR_SELECT_LABEL,
                textColor = colors.onSurface,
                onClick = onSelectionClick
            )
        }
    }
}

@Composable
fun DefaultAlarmsListTopBarCompactHeight(
    modifier: Modifier = Modifier,
    nextAlarmTime: LocalDateTime?,
    onNewAlarmClick: () -> Unit,
    onSelectionClick: () -> Unit

) {
    val colors = LocalAlarmMeColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TopBarContentHorizontalPadding,
                vertical = TopBarContentVerticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
    ) {
        val topBarEyebrow = remember(nextAlarmTime) { nextAlarmTime.toTopBarEyebrow() }

        Box(Modifier.weight(1f)){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
            ) {
                Text(
                    text = TOP_BAR_TITLE,
                    style = LocalAlarmMeTypography.current.headlineSmall,
                    color = colors.onSurface,
                    modifier = Modifier
                )

                Text(
                    text = topBarEyebrow,
                    style = LocalAlarmMeTypography.current.labelSmall,
                    color = colors.primary
                )
            }
        }

        AmDestructiveTextButton(
            text = TOP_BAR_SELECT_LABEL,
            textColor = colors.onSurface,
            onClick = onSelectionClick
        )

        AmFabExtendedButton(
            text = TOP_BAR_NEW_ALARM_LABEL,
            icon = { Text(text = TOP_BAR_NEW_ALARM_GLYPH) },
            onClick = onNewAlarmClick
        )
    }
}

@Composable
fun DefaultAlarmsListTopExpandedAndBiggerWidth(
    modifier: Modifier = Modifier,
    nextAlarmTime: LocalDateTime?,
    onNewAlarmClick: () -> Unit,
    onSelectionClick: () -> Unit

) {
    val colors = LocalAlarmMeColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TopBarContentHorizontalPadding,
                vertical = TopBarContentVerticalPadding
            )
    ) {
        val topBarEyebrow = remember(nextAlarmTime) { nextAlarmTime.toTopBarEyebrow() }
        Text(
            text = topBarEyebrow,
            style = LocalAlarmMeTypography.current.labelSmall,
            color = colors.primary
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = TOP_BAR_TITLE,
                style = LocalAlarmMeTypography.current.headlineSmall,
                color = colors.onSurface,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
            ){
                AmDestructiveTextButton(
                    text = TOP_BAR_SELECT_LABEL,
                    textColor = colors.onSurface,
                    onClick = onSelectionClick
                )

                AmFabExtendedButton(
                    text = TOP_BAR_NEW_ALARM_LABEL,
                    icon = { Text(text = TOP_BAR_NEW_ALARM_GLYPH) },
                    onClick = onNewAlarmClick
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    val currentMoment = Clock.System.now()
    val systemZone = TimeZone.currentSystemDefault()
    return currentMoment.toLocalDateTime(systemZone)
}

@OptIn(ExperimentalTime::class)
fun LocalDateTime?.toTopBarEyebrow(
    now: LocalDateTime = LocalDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    if (this == null) return "NO ALARMS ARMED"

    // Subtract instants, not individual fields. Per-field subtraction goes negative whenever the
    // alarm's minute is below the current minute (6:40 vs 7:50 -> "-1H -10M") and breaks entirely
    // across midnight. Clamped at zero so an alarm that has just fired never counts backwards.
    val remaining = (toInstant(timeZone) - now.toInstant(timeZone)).coerceAtLeast(Duration.ZERO)
    val remainingHours = remaining.inWholeHours
    val remainingMinutes = remaining.inWholeMinutes % 60

    // 0h and 12h are both "12" on a 12-hour clock; a bare `hour % 12` renders midnight as "0:40".
    val hourInTwelves = if (hour % 12 == 0) 12 else hour % 12
    val minutes = minute.toString().padStart(2, '0')
    val meridiem = if (hour >= 12) "PM" else "AM"

    return "NEXT · $hourInTwelves:$minutes $meridiem · IN ${remainingHours}H ${remainingMinutes}M"
}

/**
 * A next-alarm time a fixed distance ahead of now, so previews always render a sensible countdown
 * no matter what time of day they are opened.
 */
@OptIn(ExperimentalTime::class)
private fun previewNextAlarmTime(): LocalDateTime =
    (Clock.System.now() + 7.hours + 26.minutes).toLocalDateTime(TimeZone.currentSystemDefault())



@Preview(name = "3 selected - Light", widthDp = 412, showBackground = true)
@Preview(
    name = "3 selected - Dark",
    widthDp = 412,
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmsListTopBarSelectionModeMultiSelectionPreview() {
    AlarmMeThemeProvider {
        AlarmsListTopBarSelectionMode(
            modifier = Modifier,
            selectedAlarmsCount = 3,
            onDismiss = {},
            onSelectAll = {}
        )
    }
}

/**
 * The default (non-selection) bar across screen categories. [CommonScreenCategoriesPreview]'s three
 * buckets happen to hit all three dispatch branches exactly once: medium height + compact width ->
 * the compact/medium-width variant, compact height -> the single-row variant, and expanded width ->
 * the expanded variant.
 */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmsListTopBarDefaultPreview() {
    AlarmMeThemeProvider {
        AlarmsListTopBar(
            selectedAlarmsCount = 0,
            nextAlarmTime = previewNextAlarmTime(),
            onDismissSelection = {},
            onSelectAll = {},
            onSelectionClick = {},
            onNewAlarmClick = {}
        )
    }
}

/** No alarm armed - the eyebrow falls back to its empty text. */
@CompactWidthMediumHeightPreview
@Composable
private fun AlarmsListTopBarNoNextAlarmPreview() {
    AlarmMeThemeProvider {
        AlarmsListTopBar(
            selectedAlarmsCount = 0,
            nextAlarmTime = null,
            onDismissSelection = {},
            onSelectAll = {},
            onSelectionClick = {},
            onNewAlarmClick = {}
        )
    }
}

/** Selection mode - identical at every screen category, so one bucket is enough. */
@CompactWidthMediumHeightPreview
@Composable
private fun AlarmsListTopBarSelectionPreview() {
    AlarmMeThemeProvider {
        AlarmsListTopBar(
            selectedAlarmsCount = 3,
            nextAlarmTime = previewNextAlarmTime(),
            onDismissSelection = {},
            onSelectAll = {},
            onSelectionClick = {},
            onNewAlarmClick = {}
        )
    }
}
