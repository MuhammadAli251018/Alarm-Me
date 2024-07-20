package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection.Date
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    isVisible: Boolean = true,
    onSelectDate: (LocalDate) -> Unit = {_ ->},
    onNegativeClick: () -> Unit = {}
) {


    // used that approach when you don't provide close or dismiss request
    val state by remember { derivedStateOf { UseCaseState(isVisible) }}

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
