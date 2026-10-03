package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

/** A neutral icon button — semi-transparent surface chip, for chrome like dismiss/close. */
@Composable
fun AmIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    glyphStyle: TextStyle = LocalAlarmMeTypography.current.bodyLarge,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalAlarmMeColors.current

    BaseIconButton(
        modifier = modifier,
        colors = AmButtonColors.solid(
            containerColor = Color.Transparent,
            contentColor = colors.onSurface
        ),
        glyphStyle = glyphStyle,
        enabled = enabled,
        selected = selected,
        interactionSource = interactionSource,
        onClick = onClick,
        content = content
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmIconButtonPreview() {
    AlarmMeThemeProvider {
        AmIconButton(
            modifier = Modifier.padding(10.dp),
            onClick = {}
        ) {
            AmIcon(AlarmMeIcons.Close, contentDescription = null)
        }
    }
}
