package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.border
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
fun AmOutlineTextButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val buttonColors = AmButtonColors.outlined(colors.onSurface)
    val borderColor = if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor

    BaseTextButton(
        modifier = modifier
            .selectedOutline(
                color = colors.primary,
                shape = LocalAlarmMeShapes.current.full,
                isSelected = { selected }
            )
            .border(1.dp, borderColor, LocalAlarmMeShapes.current.full),
        text = text,
        colors = buttonColors,
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
private fun AmOutlineTextButtonPreview() {
    AlarmMeThemeProvider {
        AmOutlineTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Cancel",
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
private fun AmOutlineTextButtonSelectedPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmOutlineTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Cancel",
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
private fun AmOutlineTextButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmOutlineTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Cancel",
            enabled = false,
            onClick = {}
        )
    }
}
