package dev.alidroid.alarmme.ui.component

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.ui.theme.PlexMono

/** The pill's metrics, exposed so placeholders can reserve exactly the space the real FAB takes. */
object AmFabExtendedButtonDefaults {
    val Height: Dp = 56.dp
    val Shape: RoundedCornerShape = RoundedCornerShape(20.dp)
    val ContentHorizontalPadding: Dp = 24.dp
    val IconGap: Dp = 8.dp
}

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

    BaseFabButton(
        colors = AmButtonColors.solid(colors.primary, colors.onPrimary),
        shape = AmFabExtendedButtonDefaults.Shape,
        glyphStyle = FabExtendedGlyphStyle,
        modifier = modifier.height(AmFabExtendedButtonDefaults.Height),
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AmFabExtendedButtonDefaults.ContentHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(AmFabExtendedButtonDefaults.IconGap),
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
            icon = { AmIcon(AlarmMeIcons.Add, null) },
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
