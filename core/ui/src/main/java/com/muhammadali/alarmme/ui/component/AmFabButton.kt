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

private val FabSize = 64.dp

/** A squircle, not a circle — it echoes the alarm card radius rather than the M3 FAB. */
internal val FabShape = RoundedCornerShape(22.dp)

internal val FabGlyphStyle = TextStyle(
    fontFamily = PlexMono,
    fontWeight = FontWeight.Light,
    fontSize = 30.sp,
    lineHeight = 30.sp
)

/**
 * The primary "add alarm" affordance on compact windows: a 64dp squircle carrying a single glyph.
 *
 * On medium windows this becomes [AmFabExtendedButton]; on expanded it is replaced by an inline top-bar
 * button and does not appear at all.
 */
@Composable
fun AmFabButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val containerColor =
        if (enabled) colors.primary else colors.onSurface.copy(alpha = DisabledContainerAlpha)
    val contentColor =
        if (enabled) colors.onPrimary else colors.onSurface.copy(alpha = DisabledContentAlpha)

    BaseFabButton(
        containerColor = containerColor,
        contentColor = contentColor,
        shape = FabShape,
        glyphStyle = FabGlyphStyle,
        modifier = modifier.size(FabSize),
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
private fun AmFabButtonPreview() {
    AlarmMeThemeProvider {
        AmFabButton(
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
private fun AmFabButtonPressedPreview() {
    AlarmMeThemeProvider {
        AmFabButton(
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
private fun AmFabButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmFabButton(
            modifier = Modifier.padding(10.dp),
            enabled = false,
            onClick = {}
        ) {
            Text(text = "＋")
        }
    }
}
