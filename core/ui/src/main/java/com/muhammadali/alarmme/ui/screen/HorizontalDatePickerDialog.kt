package com.muhammadali.alarmme.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.muhammadali.alarmme.ui.component.AmHorizontalDatePicker
import com.muhammadali.alarmme.ui.component.AmPlainTextButton
import com.muhammadali.alarmme.ui.component.AmSquaryFilledTextButton
import com.muhammadali.alarmme.ui.component.previewSelectedDates
import com.muhammadali.alarmme.ui.component.rememberDatePickerCalendarState
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalDate

@Composable
fun HorizontalDatePickerDialog(
    selectedDates: Set<LocalDate>,
    onDateToggle: (LocalDate) -> Unit,
    onSave: (Set<LocalDate>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    state: CalendarState = rememberDatePickerCalendarState(),
) {
    val typography = LocalAlarmMeTypography.current

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.padding(horizontal = 16.dp),
        title = { Text(text = "Set Time", style = typography.titleLarge) },
        text = {
            AmHorizontalDatePicker(
                modifier = Modifier.height(300.dp),
                selectedDates = selectedDates,
                onDateToggle = onDateToggle,
                state = state
            )
        },
        confirmButton = {
            AmSquaryFilledTextButton(text = "Save", onClick = { onSave(selectedDates) })
        },
        dismissButton = {
            AmPlainTextButton(text = "Cancel", onClick = onDismiss)
        }
    )
}

@CommonScreenCategoriesPreview
@Composable
private fun HorizontalDatePickerDialogPreview() {
    AlarmMeThemeProvider {
        val dates = remember { previewSelectedDates().toMutableStateList() }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)){
            HorizontalDatePickerDialog(
                selectedDates = dates.toSet(),
                onDateToggle = { if (it in dates) dates.remove(it) else dates.add(it) },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}
