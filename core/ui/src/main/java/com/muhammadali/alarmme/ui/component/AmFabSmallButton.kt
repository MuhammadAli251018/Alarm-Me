package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.PlexMono

private val FabSmallSize = 40.dp
private val FabSmallShape = RoundedCornerShape(14.dp)

private val FabSmallGlyphStyle = TextStyle(
    fontFamily = PlexMono,
    fontWeight = FontWeight.Light,
    fontSize = 20.sp,
    lineHeight = 20.sp
)

/**
 * A 40dp [AmFabButton] for secondary placements, where a 64dp squircle would dominate the surface it
 * sits on. Same container and glyph treatment, one step down in size and radius.
 *
 * At 40dp the drawn shape is under the 48dp minimum touch target — callers placing this on a
 * touch surface should give it the room, e.g. `Modifier.minimumInteractiveComponentSize()`.
 */
@Composable
fun AmFabSmallButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalAlarmMeColors.current

    BaseFabButton(
        colors = AmButtonColors.solid(colors.primary, colors.onPrimary),
        shape = FabSmallShape,
        glyphStyle = FabSmallGlyphStyle,
        modifier = modifier.size(FabSmallSize),
        enabled = enabled,
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
private fun AmFabSmallButtonPreview() {
    AlarmMeThemeProvider {
        AmFabSmallButton(
            modifier = Modifier.padding(10.dp),
            onClick = {}
        ) {
            Text(text = "＋")
        }
    }
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(
    name = "Dark Mode--Pressed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFabSmallButtonPressedPreview() {
    AlarmMeThemeProvider {
        AmFabSmallButton(
            modifier = Modifier.padding(10.dp),
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        ) {
            Text(text = "＋")
        }
    }
}

@Preview(name = "Light Mode--Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFabSmallButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmFabSmallButton(
            modifier = Modifier.padding(10.dp),
            enabled = false,
            onClick = {}
        ) {
            Text(text = "＋")
        }
    }
}
