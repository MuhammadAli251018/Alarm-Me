package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIfElse

@Composable
fun AmChipRow(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shapes.medium)
            .modifyIfElse(
                condition = selected,
                thenModifier = {
                    background(colors.primary)
                },
                otherwiseModifier = {
                    border(1.dp, colors.outlineVariant, shapes.medium)
                }
            )
            .toggleable(
                value = selected,
                enabled = enabled,
                onValueChange = { onToggle() }
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

        Text(
            text = text,
            style = typography.titleMedium,
            color = contentColor
        )
    }
}

@Preview(name = "Light Mode--Unselected", showBackground = true)
@Preview(
    name = "Dark Mode--Unselected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChipRowUnselectedPreview() {
    var selected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmChipRow(
            modifier = Modifier.padding(10.dp),
            text = "9 min",
            selected = selected,
            enabled = true,
            onToggle = { selected = !selected }
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChipRowSelectedPreview() {
    var selected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmChipRow(
            modifier = Modifier.padding(10.dp),
            text = "9 min",
            selected = selected,
            enabled = true,
            onToggle = { selected = !selected }
        )
    }
}

@Preview(name = "Light Mode--Disabled Unselected", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Unselected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChipRowDisabledUnselectedPreview() {
    var selected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmChipRow(
            modifier = Modifier.padding(10.dp),
            text = "9 min",
            selected = selected,
            enabled = false,
            onToggle = { selected = !selected }
        )
    }
}

@Preview(name = "Light Mode--Disabled Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChipRowDisabledSelectedPreview() {
    var selected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmChipRow(
            modifier = Modifier.padding(10.dp),
            text = "9 min",
            selected = selected,
            enabled = false,
            onToggle = { selected = !selected }
        )
    }
}