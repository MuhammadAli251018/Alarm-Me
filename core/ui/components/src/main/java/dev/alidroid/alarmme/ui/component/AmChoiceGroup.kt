package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider

@Stable
enum class Orientation { Vertical, Horizontal }

@Composable
fun AmChoiceGroup(
    label: String,
    options: List<String>,
    selectedOption: Int?,
    orientation: Orientation,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onOptionSelected: (Int) -> Unit
) {
    when (orientation) {
        Orientation.Vertical -> AmChoiceGroupVertical(
            label = label,
            options = options,
            selectedOption = selectedOption,
            modifier = modifier,
            enabled = enabled,
            onOptionSelected = onOptionSelected
        )

        Orientation.Horizontal -> AmChoiceGroupHorizontal(
            label = label,
            options = options,
            selectedOption = selectedOption,
            modifier = modifier,
            enabled = enabled,
            onOptionSelected = onOptionSelected
        )
    }
}

@Composable
private fun AmChoiceGroupVertical(
    label: String,
    options: List<String>,
    selectedOption: Int?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AmSectionLabel(text = label)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEachIndexed { index, option ->
                AmChipRow(
                    text = option,
                    selected = index == selectedOption,
                    enabled = enabled,
                    onToggle = { onOptionSelected(index) }
                )
            }
        }
    }
}

@Composable
private fun AmChoiceGroupHorizontal(
    label: String,
    options: List<String>,
    selectedOption: Int?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AmSectionLabel(text = label)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEachIndexed { index, option ->
                AmChip(
                    modifier = Modifier.weight(1f),
                    text = option,
                    selected = index == selectedOption,
                    enabled = enabled,
                    onToggle = { onOptionSelected(index) }
                )
            }
        }
    }
}

private val snoozeLengths = listOf("5 min", "9 min", "10 min", "15 min", "20 min")
private val snoozeLimits = listOf("1", "2", "3", "5", "∞")

@Preview(name = "Light Mode--Vertical", showBackground = true)
@Preview(
    name = "Dark Mode--Vertical",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChoiceGroupVerticalPreview() {
    var selected by remember { mutableIntStateOf(1) }

    AlarmMeThemeProvider {
        AmChoiceGroup(
            modifier = Modifier.padding(10.dp),
            label = "Snooze for",
            options = snoozeLengths,
            selectedOption = selected,
            orientation = Orientation.Vertical,
            onOptionSelected = { selected = it }
        )
    }
}

@Preview(name = "Light Mode--Vertical Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Vertical Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChoiceGroupVerticalDisabledPreview() {
    var selected by remember { mutableIntStateOf(1) }

    AlarmMeThemeProvider {
        AmChoiceGroup(
            modifier = Modifier.padding(10.dp),
            label = "Snooze for",
            options = snoozeLengths,
            selectedOption = selected,
            orientation = Orientation.Vertical,
            enabled = false,
            onOptionSelected = { selected = it }
        )
    }
}

@Preview(name = "Light Mode--Horizontal", showBackground = true)
@Preview(
    name = "Dark Mode--Horizontal",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChoiceGroupHorizontalPreview() {
    var selected by remember { mutableIntStateOf(2) }

    AlarmMeThemeProvider {
        AmChoiceGroup(
            modifier = Modifier.padding(10.dp),
            label = "At most",
            options = snoozeLimits,
            selectedOption = selected,
            orientation = Orientation.Horizontal,
            onOptionSelected = { selected = it }
        )
    }
}

@Preview(name = "Light Mode--Horizontal Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Horizontal Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChoiceGroupHorizontalDisabledPreview() {
    var selected by remember { mutableIntStateOf(2) }

    AlarmMeThemeProvider {
        AmChoiceGroup(
            modifier = Modifier.padding(10.dp),
            label = "At most",
            options = snoozeLimits,
            selectedOption = selected,
            orientation = Orientation.Horizontal,
            enabled = false,
            onOptionSelected = { selected = it }
        )
    }
}
