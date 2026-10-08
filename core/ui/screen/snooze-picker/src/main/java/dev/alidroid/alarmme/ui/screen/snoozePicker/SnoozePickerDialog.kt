package dev.alidroid.alarmme.ui.screen.snoozePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmChoiceGroup
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.component.Orientation
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun SnoozePickerDialog(
    title: String,
    selectedLengthIndex: Int?,
    selectedLimitIndex: Int?,
    onLengthIndexSelected: (Int) -> Unit,
    onLimitIndexSelected: (Int) -> Unit,
    onTurnOff: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    snoozeLimitOptions: List<Int?> = SnoozePickerOptions.Defaults.snoozeLimitOptions,
    snoozeLengthOptions: List<Duration> = SnoozePickerOptions.Defaults.snoozeLengthOptions,
    subtitle: String? = null,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val limitEnabled = selectedLengthIndex != null

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = colors.surfaceContainerHigh,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(text = title, style = typography.titleLarge)

                subtitle?.let {
                    Text(text = it, style = typography.bodyMedium, color = colors.onSurfaceVariant)
                }

                AmChoiceGroup(
                    label = "Snooze for",
                    options = snoozeLengthOptions.map { "${it.inWholeMinutes} min" },
                    selectedOption = selectedLengthIndex,
                    orientation = Orientation.Vertical,
                    onOptionSelected = onLengthIndexSelected
                )

                AmChoiceGroup(
                    label = "At most",
                    options = snoozeLimitOptions.map { it?.toString() ?: "∞" },
                    selectedOption = selectedLimitIndex,
                    orientation = Orientation.Horizontal,
                    enabled = limitEnabled,
                    onOptionSelected = onLimitIndexSelected
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (selectedLengthIndex != null) {
                        AmPlainTextButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "Turn off",
                            contentColor = colors.primary,
                            onClick = onTurnOff
                        )
                    }

                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AmPlainTextButton(text = "Cancel", onClick = onDismiss)
                        AmFilledTextButton(text = "Save", onClick = onSave)
                    }
                }
            }
        }
    )
}

class SnoozePickerOptions(
    val snoozeLengthOptions: List<Duration>,
    val snoozeLimitOptions: List<Int?>
) {
    companion object {
        val Defaults = SnoozePickerOptions(SnoozeLengthOptions, SnoozeLimitOptions)
    }
}

/** The fixed set of snooze lengths per the design spec, shared by every screen that opens this dialog. */
internal val SnoozeLengthOptions: List<Duration> = listOf(5, 9, 10, 15, 20).map { it.minutes }

/** The fixed set of snooze limits per the design spec; `null` is the "∞ / no limit" box. */
internal val SnoozeLimitOptions: List<Int?> = listOf(1, 2, 3, 5, null)

private val PreviewSnoozeLengthOptions = SnoozeLengthOptions
private val PreviewSnoozeLimitOptions = SnoozeLimitOptions

@CommonScreenCategoriesPreview
@Composable
private fun SnoozePickerDialogPreview() {
    AlarmMeThemeProvider {
        var selectedLengthIndex by remember { mutableStateOf<Int?>(1) }
        var selectedLimitIndex by remember { mutableStateOf<Int?>(2) }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            SnoozePickerDialog(
                title = "Default snooze",
                snoozeLengthOptions = PreviewSnoozeLengthOptions,
                snoozeLimitOptions = PreviewSnoozeLimitOptions,
                selectedLengthIndex = selectedLengthIndex,
                selectedLimitIndex = selectedLimitIndex,
                onLengthIndexSelected = { selectedLengthIndex = it },
                onLimitIndexSelected = { selectedLimitIndex = it },
                onTurnOff = { selectedLengthIndex = null },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun SnoozePickerDialogOffPreview() {
    AlarmMeThemeProvider {
        var selectedLengthIndex by remember { mutableStateOf<Int?>(null) }
        var selectedLimitIndex by remember { mutableStateOf<Int?>(2) }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            SnoozePickerDialog(
                title = "Default snooze",
                snoozeLengthOptions = PreviewSnoozeLengthOptions,
                snoozeLimitOptions = PreviewSnoozeLimitOptions,
                selectedLengthIndex = selectedLengthIndex,
                selectedLimitIndex = selectedLimitIndex,
                onLengthIndexSelected = { selectedLengthIndex = it },
                onLimitIndexSelected = { selectedLimitIndex = it },
                onTurnOff = { selectedLengthIndex = null },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun SnoozePickerDialogSettingsPreview() {
    AlarmMeThemeProvider {
        var selectedLengthIndex by remember { mutableStateOf<Int?>(1) }
        var selectedLimitIndex by remember { mutableStateOf<Int?>(2) }

        Box(Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)) {
            SnoozePickerDialog(
                title = "Default snooze",
                subtitle = "New alarms start with this. Existing alarms don't change.",
                snoozeLengthOptions = PreviewSnoozeLengthOptions,
                snoozeLimitOptions = PreviewSnoozeLimitOptions,
                selectedLengthIndex = selectedLengthIndex,
                selectedLimitIndex = selectedLimitIndex,
                onLengthIndexSelected = { selectedLengthIndex = it },
                onLimitIndexSelected = { selectedLimitIndex = it },
                onTurnOff = { selectedLengthIndex = null },
                onSave = {},
                onDismiss = {}
            )
        }
    }
}
