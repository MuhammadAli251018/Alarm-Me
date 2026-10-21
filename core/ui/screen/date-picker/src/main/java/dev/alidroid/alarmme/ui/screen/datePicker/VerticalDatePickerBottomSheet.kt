package dev.alidroid.alarmme.ui.screen.datePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.datetime.LocalDate

/** [AmVerticalDatePicker] inside a bottom sheet, with the Save/Cancel actions it doesn't own. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalDatePickerBottomSheet(
    selectedDates: Set<LocalDate>,
    onDateToggle: (LocalDate) -> Unit,
    onSave: (Set<LocalDate>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    state: CalendarState = rememberDatePickerCalendarState(),
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = LocalAlarmMeColors.current.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AmVerticalDatePicker(
                selectedDates = selectedDates,
                onDateToggle = onDateToggle,
                state = state
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                AmPlainTextButton(text = stringResource(ComponentsR.string.common_cancel), onClick = onDismiss)
                AmFilledTextButton(text = stringResource(ComponentsR.string.common_save), onClick = { onSave(selectedDates) })
            }
        }
    }
}

@Preview
@CommonScreenCategoriesPreview
@Composable
private fun VerticalDatePickerBottomSheetPreview() {
    AlarmMeThemeProvider {
        val dates = remember { previewSelectedDates().toMutableStateList() }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            VerticalDatePickerBottomSheet(
                selectedDates = dates.toSet(),
                onDateToggle = { if (it in dates) dates.remove(it) else dates.add(it) },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}
