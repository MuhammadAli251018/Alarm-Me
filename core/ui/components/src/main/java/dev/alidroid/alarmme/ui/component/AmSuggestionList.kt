package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider

private const val MaxSuggestions = 6

@Composable
fun AmSuggestionList(
    label: String,
    suggestions: List<String>,
    value: String,
    onSuggestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSuggestionLongClick: ((String) -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AmSectionLabel(text = label)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.take(MaxSuggestions).forEach { suggestion ->
                AmSuggestionChip(
                    text = suggestion,
                    selected = suggestion.equals(value, ignoreCase = true),
                    onClick = { onSuggestionSelected(suggestion) },
                    onLongClick = onSuggestionLongClick?.let { callback -> { callback(suggestion) } }
                )
            }
        }
    }
}

private val RecentLabels = listOf("Gym", "Medication", "Weekday mornings", "Dentist")
private val IdeaLabels = listOf("Wake up", "Work", "Gym", "Medication", "Nap")

@Preview(name = "Light Mode--Recent", showBackground = true)
@Preview(
    name = "Dark Mode--Recent",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionListRecentPreview() {
    var value by remember { mutableStateOf("Gym") }
    var suggestions by remember { mutableStateOf(RecentLabels) }

    AlarmMeThemeProvider {
        AmSuggestionList(
            modifier = Modifier.padding(10.dp),
            label = "Recent",
            suggestions = suggestions,
            value = value,
            onSuggestionSelected = { value = it },
            onSuggestionLongClick = { suggestions = suggestions.filterNot { it == value } }
        )
    }
}

@Preview(name = "Light Mode--Ideas", showBackground = true)
@Preview(
    name = "Dark Mode--Ideas",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionListIdeasPreview() {
    var value by remember { mutableStateOf("") }

    AlarmMeThemeProvider {
        AmSuggestionList(
            modifier = Modifier.padding(10.dp),
            label = "Ideas",
            suggestions = IdeaLabels,
            value = value,
            onSuggestionSelected = { value = it }
        )
    }
}

@Preview(name = "Light Mode--Overflow", showBackground = true)
@Preview(
    name = "Dark Mode--Overflow",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionListOverflowPreview() {
    var value by remember { mutableStateOf("") }
    val manyLabels = listOf("Gym", "Medication", "Weekday mornings", "Dentist", "Nap", "Work", "Study")

    AlarmMeThemeProvider {
        AmSuggestionList(
            modifier = Modifier.padding(10.dp),
            label = "Recent",
            suggestions = manyLabels,
            value = value,
            onSuggestionSelected = { value = it },
            onSuggestionLongClick = {}
        )
    }
}