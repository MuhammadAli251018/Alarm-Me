package com.muhammadali.alarmme.ui.screen.datePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

private val ColumnGap = 20.dp
private val SelectedColumnWidth = 80.dp
private val DividerWidth = 1.dp

@Composable
fun AmHorizontalDatePicker(
    selectedDates: Set<LocalDate>,
    onDateToggle: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: CalendarState = rememberDatePickerCalendarState(),
) {
    val colors = LocalAlarmMeColors.current
    val scope = rememberCoroutineScope()
    val today = rememberToday()
    val weekDays = remember(state.firstDayOfWeek) {
        daysOfWeek(firstDayOfWeek = state.firstDayOfWeek)
    }
    val visibleMonth = state.firstVisibleMonth.yearMonth

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else DisabledDatePickerAlpha),
        horizontalArrangement = Arrangement.spacedBy(ColumnGap)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePickerMonthHeader(
                month = visibleMonth,
                enabled = enabled,
                canGoPrevious = state.canGoPreviousMonth,
                canGoNext = state.canGoNextMonth,
                onPreviousClick = {
                    scope.launch { state.animateScrollToMonth(visibleMonth.previousMonth) }
                },
                onNextClick = {
                    scope.launch { state.animateScrollToMonth(visibleMonth.nextMonth) }
                }
            )

            DatePickerWeekdayHeader(daysOfWeek = weekDays)

            HorizontalCalendar(
                state = state,
                userScrollEnabled = enabled,
                contentHeightMode = ContentHeightMode.Wrap,
                dayContent = { day ->
                    DatePickerDayCell(
                        modifier = Modifier.padding(2.dp),
                        day = day,
                        selected = selectedDates.contains(day.date.toKotlinLocalDate()),
                        isToday = day.date.toKotlinLocalDate() == today,
                        enabled = enabled,
                        onClick = onDateToggle
                    )
                }
            )
        }

        Box(
            modifier = Modifier
                .width(DividerWidth)
                .fillMaxHeight()
                .background(colors.outline.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier.weight(0.35f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectedDatesEyebrow(count = selectedDates.size)

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(
                    items = selectedDates.sorted(),
                    key = { it.toString() }
                ) { date ->
                    SelectedDateChip(
                        modifier = Modifier
                            .sizeIn(minWidth = 85.dp)
                            .fillMaxWidth(0.5f),
                        date = date,
                        enabled = enabled,
                        onRemove = { onDateToggle(date) }
                    )
                }
            }
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmHorizontalDatePickerPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            val dates = remember { previewSelectedDates().toMutableStateList() }

            AmHorizontalDatePicker(
                modifier = Modifier.height(300.dp),
                selectedDates = dates.toSet(),
                onDateToggle = { if (it in dates) dates.remove(it) else dates.add(it) },
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmHorizontalDatePickerEmptyPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            AmHorizontalDatePicker(
                modifier = Modifier.height(230.dp),
                selectedDates = emptySet(),
                onDateToggle = {},
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmHorizontalDatePickerDisabledPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            AmHorizontalDatePicker(
                modifier = Modifier.height(300.dp),
                selectedDates = previewSelectedDates().toSet(),
                onDateToggle = {},
                enabled = false,
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}
