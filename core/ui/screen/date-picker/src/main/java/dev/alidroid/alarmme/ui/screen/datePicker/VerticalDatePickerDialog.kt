package dev.alidroid.alarmme.ui.screen.datePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kizitonwose.calendar.compose.CalendarState
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screen.datePicker.R
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalDate

/** [AmVerticalDatePicker] inside an alert dialog, with the Save/Cancel actions it doesn't own. */
@Composable
fun VerticalDatePickerDialog(
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
        modifier = modifier,
        containerColor = LocalAlarmMeColors.current.surface,
        title = { Text(text = stringResource(R.string.date_picker_dialog_title), style = typography.titleLarge) },
        text = {
            AmVerticalDatePicker(
                selectedDates = selectedDates,
                onDateToggle = onDateToggle,
                state = state
            )
        },
        confirmButton = {
            AmFilledTextButton(text = stringResource(ComponentsR.string.common_save), onClick = { onSave(selectedDates) })
        },
        dismissButton = {
            AmPlainTextButton(text = stringResource(ComponentsR.string.common_cancel), onClick = onDismiss)
        }
    )
}

@CommonScreenCategoriesPreview
@Composable
private fun VerticalDatePickerDialogPreview() {
    AlarmMeThemeProvider {
        val dates = remember { previewSelectedDates().toMutableStateList() }
        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            VerticalDatePickerDialog(
                selectedDates = dates.toSet(),
                onDateToggle = { if (it in dates) dates.remove(it) else dates.add(it) },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}
