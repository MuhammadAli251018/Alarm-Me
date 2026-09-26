package com.muhammadali.alarmme.ui.screen.datePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.kizitonwose.calendar.core.yearMonth
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun AmVerticalDatePicker(
    selectedDates: Set<LocalDate>,
    onDateToggle: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: CalendarState = rememberDatePickerCalendarState(),
) {
    val scope = rememberCoroutineScope()
    val today = rememberToday()
    val weekDays = remember(state.firstDayOfWeek) {
        daysOfWeek(firstDayOfWeek = state.firstDayOfWeek)
    }
    val visibleMonth = state.firstVisibleMonth.yearMonth

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else DisabledDatePickerAlpha)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    day = day,
                    selected = selectedDates.contains(day.date.toKotlinLocalDate()),
                    isToday = day.date.toKotlinLocalDate() == today,
                    enabled = enabled,
                    onClick = onDateToggle
                )
            }
        )

        SelectedDatesEyebrow(count = selectedDates.size)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                items = selectedDates.sorted(),
                key = { it.toString() }
            ) { date ->
                SelectedDateChip(
                    date = date,
                    enabled = enabled,
                    onRemove = { onDateToggle(date) }
                )
            }
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmVerticalDatePickerPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            val dates = remember { previewSelectedDates().toMutableStateList() }

            AmVerticalDatePicker(
                modifier = Modifier.width(320.dp),
                selectedDates = dates.toSet(),
                onDateToggle = { if (it in dates) dates.remove(it) else dates.add(it) },
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmVerticalDatePickerEmptyPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            AmVerticalDatePicker(
                modifier = Modifier.width(320.dp),
                selectedDates = emptySet(),
                onDateToggle = {},
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun AmVerticalDatePickerDisabledPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surface)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            AmVerticalDatePicker(
                modifier = Modifier.width(320.dp),
                selectedDates = previewSelectedDates().toSet(),
                onDateToggle = {},
                enabled = false,
                state = rememberDatePickerCalendarState(initialMonth = previewMonth())
            )
        }
    }
}
