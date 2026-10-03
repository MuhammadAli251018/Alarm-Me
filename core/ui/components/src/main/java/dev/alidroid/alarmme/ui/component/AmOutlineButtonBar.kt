package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors

@Composable
fun AmOutlineButtonBar(
    modifier: Modifier = Modifier,
    text: String,
    icon: @Composable () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current

    BaseTextWithIconButton(
        modifier = modifier.fillMaxWidth(),
        text = text,
        colors = AmButtonColors.outlined(colors.outline),
        icon = icon,
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
private fun AmOutlineButtonBarPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmOutlineButtonBar(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            icon = { AmIcon(AlarmMeIcons.Add, null, tint = colors.onSurface) },
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(
    name = "Dark Mode--Pressed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmOutlineButtonBarPressedPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmOutlineButtonBar(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            icon = { AmIcon(AlarmMeIcons.Add, null, tint = colors.onSurface) },
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        )
    }
}
