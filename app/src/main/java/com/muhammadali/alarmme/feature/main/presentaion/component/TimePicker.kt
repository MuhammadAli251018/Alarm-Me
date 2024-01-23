package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    state: UseCaseState = rememberUseCaseState(true),
    onPositiveClick: (Int, Int) -> Unit = {_, _ ->},
    onNegativeClick: () -> Unit = {}
) {

    ClockDialog(state = state,
        selection = ClockSelection.HoursMinutes
            (
            onNegativeClick = onNegativeClick,
            onPositiveClick= onPositiveClick
        ),
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTimePicker() {
    AlarmMeTheme{ TimePicker() }
}
