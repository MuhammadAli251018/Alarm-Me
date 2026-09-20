package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIf
import com.muhammadali.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

internal const val DisabledDatePickerAlpha = 0.4f
private const val DefaultYearSpan = 100

private typealias JavaTimeDayOfWeek =  java.time.DayOfWeek

@Composable
fun rememberDatePickerCalendarState(
    initialMonth: LocalDate = LocalDateTime.now().date,
    yearRange: IntRange =
        (initialMonth.year - DefaultYearSpan)..(initialMonth.year + DefaultYearSpan),
    firstDayOfWeek: java.time.DayOfWeek = firstDayOfWeekFromLocale(),
): CalendarState = rememberCalendarState(
    startMonth = YearMonth.of(yearRange.first, 1),
    endMonth = YearMonth.of(yearRange.last, 12),
    firstVisibleMonth = initialMonth.toJavaLocalDate().yearMonth,
    firstDayOfWeek = firstDayOfWeek,
    outDateStyle = OutDateStyle.EndOfGrid,
)

/** Today, from the repo's existing clock helper. */
@Composable
internal fun rememberToday(): LocalDate = remember { LocalDateTime.now().date }

/** The month label, plus the previous/next month buttons — the only way to change month. */
@Composable
internal fun DatePickerMonthHeader(
    month: YearMonth,
    enabled: Boolean,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val locale = LocalConfiguration.current.locales[0]

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = month.toMonthYearLabel(locale),
            modifier = Modifier.weight(1f),
            style = typography.titleMedium,
            color = colors.onSurface
        )

        DatePickerNavButton(
            glyph = "‹",
            enabled = enabled && canGoPrevious,
            onClick = onPreviousClick
        )
        DatePickerNavButton(
            glyph = "›",
            enabled = enabled && canGoNext,
            onClick = onNextClick
        )
    }
}

@Composable
private fun DatePickerNavButton(
    glyph: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    Box(
        modifier = Modifier
            .clip(shapes.small)
            .background(colors.surfaceContainer)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = glyph,
            style = typography.titleMedium,
            color = colors.onSurface
        )
    }
}

/** The static M T W T F S S row. Sits outside the pager so it doesn't slide with the months. */
@Composable
internal fun DatePickerWeekdayHeader(
    daysOfWeek: List<JavaTimeDayOfWeek>,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val locale = LocalConfiguration.current.locales[0]

    Row(modifier = modifier.fillMaxWidth()) {
        daysOfWeek.forEach { dayOfWeek ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfWeek.getDisplayName(JavaTextStyle.NARROW, locale),
                    style = typography.labelSmall,
                    color = colors.outline,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
internal fun DatePickerDayCell(
    day: CalendarDay,
    selected: Boolean,
    isToday: Boolean,
    enabled: Boolean,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current
    val inMonth = day.position == DayPosition.MonthDate

    Box(
        modifier = modifier.padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shapes.small)
                .modifyIf(inMonth && selected) { background(colors.primaryContainer) }
                .modifyIf(inMonth && isToday && !selected) {
                    border(1.dp, colors.primary, shapes.small)
                }
                .clickable(enabled = enabled && inMonth) {
                    onClick(day.date.toKotlinLocalDate())
                }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = typography.bodyMedium,
                color = when {
                    !inMonth -> Color.Transparent
                    selected -> colors.onPrimaryContainer
                    else -> colors.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
internal fun SelectedDatesEyebrow(
    count: Int,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Text(
        text = if (count == 0) "NO DATES SELECTED" else "$count DATES SELECTED",
        modifier = modifier,
        style = typography.labelSmall,
        color = colors.outline
    )
}

@Composable
internal fun SelectedDateChip(
    date: LocalDate,
    enabled: Boolean,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current
    val locale = LocalConfiguration.current.locales[0]

    Row(
        modifier = modifier
            .clip(shapes.small)
            .background(colors.primaryContainer)
            .clickable(enabled = enabled, onClick = onRemove)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = date.toChipLabel(locale),
            style = typography.labelMedium,
            color = colors.onPrimaryContainer,
            maxLines = 1
        )
        Text(
            text = "✕",
            style = typography.labelMedium,
            color = colors.onPrimaryContainer
        )
    }
}

internal fun LocalDate.toChipLabel(locale: Locale): String {
    val month = toJavaLocalDate().month.getDisplayName(JavaTextStyle.SHORT, locale)
    return "$month $day"
}

internal fun previewMonth(): LocalDate = LocalDate(2026, 9, 1)

internal fun previewSelectedDates(): List<LocalDate> = listOf(
    LocalDate(2026, 9, 5),
    LocalDate(2026, 9, 12),
    LocalDate(2026, 9, 19),
    LocalDate(2026, 9, 26),
)

/** "September 2026" — the month header label. */
internal fun YearMonth.toMonthYearLabel(locale: Locale): String {
    val month = month.getDisplayName(JavaTextStyle.FULL, locale)
    return "$month $year"
}

/** Bounds read off the pager itself, so they can never drift from what it actually allows. */
internal val CalendarState.canGoPreviousMonth: Boolean
    get() = firstVisibleMonth.yearMonth > startMonth

internal val CalendarState.canGoNextMonth: Boolean
    get() = firstVisibleMonth.yearMonth < endMonth
