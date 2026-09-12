package com.muhammadali.alarmme.ui.component

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
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes

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
    val containerColor =
        if (enabled) colors.primaryContainer else colors.onSurface.copy(alpha = DisabledContainerAlpha)
    val contentColor =
        if (enabled) colors.onPrimaryContainer else colors.onSurface.copy(alpha = DisabledContentAlpha)

    BaseTextButton(
        text = text,
        containerColor = containerColor,
        contentColor = contentColor,
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