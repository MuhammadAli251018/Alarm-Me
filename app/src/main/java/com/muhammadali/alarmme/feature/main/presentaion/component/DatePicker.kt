package com.muhammadali.alarmme.feature.main.presentaion.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection.Date
import com.muhammadali.alarmme.common.presentation.ui.theme.AlarmMeTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    state: UseCaseState = rememberUseCaseState(true),
    onSelectDate: (LocalDate) -> Unit = {_ ->},
    onNegativeClick: () -> Unit = {}
) {

    CalendarDialog(state = state,
        selection = Date(
            onNegativeClick = onNegativeClick,
            onSelectDate = onSelectDate
        ),
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePicker() {
    AlarmMeTheme{ DatePicker() }
}
