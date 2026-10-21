package dev.alidroid.alarmme.ui.screen.timePicker

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors

/** [AmTimePicker] inside a bottom sheet, with the Save/Cancel actions it doesn't own. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
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
            AmTimePicker(
                hour = hour,
                minute = minute,
                isAm = isAm,
                onHourChange = onHourChange,
                onMinuteChange = onMinuteChange,
                onAmPmChange = onAmPmChange
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                AmPlainTextButton(text = stringResource(ComponentsR.string.common_cancel), onClick = onDismiss)
                AmFilledTextButton(text = stringResource(ComponentsR.string.common_save), onClick = onSave)
            }
        }
    }
}

@Preview
@CommonScreenCategoriesPreview
@Composable
private fun TimePickerBottomSheetPreview() {
    AlarmMeThemeProvider {
        var hour by remember { mutableStateOf(10) }
        var minute by remember { mutableStateOf(30) }
        var isAm by remember { mutableStateOf(true) }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            TimePickerBottomSheet(
                hour = hour,
                minute = minute,
                isAm = isAm,
                onHourChange = { hour = it },
                onMinuteChange = { minute = it },
                onAmPmChange = { isAm = it },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}
