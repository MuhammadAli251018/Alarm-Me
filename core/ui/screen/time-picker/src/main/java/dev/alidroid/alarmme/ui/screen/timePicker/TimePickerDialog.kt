package dev.alidroid.alarmme.ui.screen.timePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screen.timePicker.R
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

/** [AmTimePicker] inside an alert dialog, with the Save/Cancel actions it doesn't own. */
@Composable
fun TimePickerDialog(
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
    val typography = LocalAlarmMeTypography.current

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = LocalAlarmMeColors.current.surface,
        title = { Text(text = stringResource(R.string.time_picker_dialog_title), style = typography.titleLarge) },
        text = {
            AmTimePicker(
                hour = hour,
                minute = minute,
                isAm = isAm,
                onHourChange = onHourChange,
                onMinuteChange = onMinuteChange,
                onAmPmChange = onAmPmChange
            )
        },
        confirmButton = {
            AmFilledTextButton(text = stringResource(ComponentsR.string.common_save), onClick = onSave)
        },
        dismissButton = {
            AmPlainTextButton(text = stringResource(ComponentsR.string.common_cancel), onClick = onDismiss)
        }
    )
}

@CommonScreenCategoriesPreview
@Composable
private fun TimePickerDialogPreview() {
    AlarmMeThemeProvider {
        var hour by remember { mutableStateOf(10) }
        var minute by remember { mutableStateOf(30) }
        var isAm by remember { mutableStateOf(true) }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            TimePickerDialog(
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
