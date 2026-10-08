package dev.alidroid.alarmme.ui.screen.labelPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmFilledTextButton
import dev.alidroid.alarmme.ui.component.AmHeroTextField
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.component.AmSuggestionList
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography


@Composable
fun LabelPickerDialog(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suggestionsLabel: String,
    suggestions: List<String>,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    placeholder: String = "What's it for?",
    maxCharCount: Int = 30,
    onSuggestionLongClick: ((String) -> Unit)? = null,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

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
                subtitle?.let {
                    Text(text = it, style = typography.bodyMedium, color = colors.onSurfaceVariant)
                }

                AmHeroTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = value,
                    label = label,
                    placeholder = placeholder,
                    maxCharCount = maxCharCount,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxCharCount) onValueChange(newValue)
                    }
                )

                AmSuggestionList(
                    label = suggestionsLabel,
                    suggestions = suggestions,
                    value = value,
                    onSuggestionSelected = onValueChange,
                    onSuggestionLongClick = onSuggestionLongClick
                )

                Box(modifier = Modifier.fillMaxWidth()) {
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

private val RecentLabels = listOf("Gym", "Medication", "Weekday mornings", "Dentist")
private val IdeaLabels = listOf("Wake up", "Work", "Gym", "Medication", "Nap")

@CommonScreenCategoriesPreview
@Composable
private fun LabelPickerDialogPreview() {
    var value by remember { mutableStateOf("Gym") }

    AlarmMeThemeProvider {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            LabelPickerDialog(
                label = "Label",
                value = value,
                onValueChange = { value = it },
                suggestionsLabel = "Recent",
                suggestions = RecentLabels,
                onSuggestionLongClick = {},
                onSave = {},
                onDismiss = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun LabelPickerDialogEmptyPreview() {
    var value by remember { mutableStateOf("") }

    AlarmMeThemeProvider {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            LabelPickerDialog(
                label = "Label",
                value = value,
                onValueChange = { value = it },
                suggestionsLabel = "Ideas",
                suggestions = IdeaLabels,
                onSave = {},
                onDismiss = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun LabelPickerDialogSettingsPreview() {
    var value by remember { mutableStateOf("Alarm") }

    AlarmMeThemeProvider {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            LabelPickerDialog(
                label = "Default label",
                subtitle = "New alarms start with this. Leave empty for none.",
                value = value,
                onValueChange = { value = it },
                suggestionsLabel = "Recent",
                suggestions = RecentLabels,
                onSuggestionLongClick = {},
                onSave = {},
                onDismiss = {}
            )
        }
    }
}