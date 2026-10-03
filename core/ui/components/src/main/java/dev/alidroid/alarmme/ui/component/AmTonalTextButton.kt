package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes

@Composable
fun AmTonalTextButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current

    BaseTextButton(
        text = text,
        colors = AmButtonColors.solid(colors.primaryContainer, colors.onPrimaryContainer),
        modifier = modifier.selectedOutline(
            color = colors.primary,
            shape = LocalAlarmMeShapes.current.full,
            isSelected = { selected }
        ),
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmTonalTextButtonPreview() {
    AlarmMeThemeProvider {
        AmTonalTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmTonalTextButtonSelectedPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmTonalTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            selected = isSelected
        ) {
            isSelected = !isSelected
        }
    }
}

@Preview(name = "Light Mode--Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmTonalTextButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmTonalTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            enabled = false,
            onClick = {}
        )
    }
}