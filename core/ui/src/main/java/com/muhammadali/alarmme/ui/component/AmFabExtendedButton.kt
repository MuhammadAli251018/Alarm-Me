package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.ui.theme.PlexMono

private val FabExtendedHeight = 56.dp
private val FabExtendedShape = RoundedCornerShape(20.dp)
private val FabExtendedHorizontalPadding = 24.dp
private val FabExtendedIconGap = 8.dp

private val FabExtendedGlyphStyle = TextStyle(
    fontFamily = PlexMono,
    fontWeight = FontWeight.Light,
    fontSize = 20.sp,
    lineHeight = 20.sp
)

/**
 * The medium-window form of [AmFabButton]: the same container, widened to carry a label so the action
 * names itself once there is room for it.
 *
 * [icon] is an optional leading slot; when present it is styled as a FAB glyph, matching
 * [AmFabSmallButton]. The label itself is always `labelLarge`, as on every other button in the system.
 */
@Composable
fun AmFabExtendedButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val containerColor =
        if (enabled) colors.primary else colors.onSurface.copy(alpha = DisabledContainerAlpha)
    val contentColor =
        if (enabled) colors.onPrimary else colors.onSurface.copy(alpha = DisabledContentAlpha)

    BaseFabButton(
        containerColor = containerColor,
        contentColor = contentColor,
        shape = FabExtendedShape,
        glyphStyle = FabExtendedGlyphStyle,
        modifier = modifier.height(FabExtendedHeight),
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = FabExtendedHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(FabExtendedIconGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()

            Text(
                text = text,
                style = LocalAlarmMeTypography.current.labelLarge
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFabExtendedButtonPreview() {
    AlarmMeThemeProvider {
        AmFabExtendedButton(
            modifier = Modifier.padding(10.dp),
            text = "New alarm",
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Icon", showBackground = true)
@Preview(
    name = "Dark Mode--Icon",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFabExtendedButtonIconPreview() {
    AlarmMeThemeProvider {
        AmFabExtendedButton(
            modifier = Modifier.padding(10.dp),
            text = "New alarm",
            icon = { Text(text = "＋") },
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
private fun AmFabExtendedButtonPressedPreview() {
    AlarmMeThemeProvider {
        AmFabExtendedButton(
            modifier = Modifier.padding(10.dp),
            text = "New alarm",
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFabExtendedButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmFabExtendedButton(
            modifier = Modifier.padding(10.dp),
            text = "New alarm",
            enabled = false,
            onClick = {}
        )
    }
}
