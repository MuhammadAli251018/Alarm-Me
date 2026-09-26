package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

/**
 * A status pill with two independent flags instead of a fixed palette per meaning:
 * [enabled] toggles the muted/"off" look (wins over [highlighted] when `false`), and
 * [highlighted] steps an enabled badge up to a filled, emphasized look.
 */
@Composable
fun AmBadge(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    highlighted: Boolean = false,
) {
    val colors = LocalAlarmMeColors.current

    val badgeColors = when {
        !enabled -> AmBadgeColors(
            containerColor = Color.Transparent,
            contentColor = colors.onSurfaceVariant,
            borderColor = colors.outlineVariant
        )

        highlighted -> AmBadgeColors(
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer,
            borderColor = Color.Transparent
        )

        else -> AmBadgeColors(
            containerColor = Color.Transparent,
            contentColor = colors.primary,
            borderColor = colors.primary
        )
    }

    BaseBadge(
        modifier = modifier,
        text = text,
        colors = badgeColors
    )
}

@Preview(name = "Light Mode--Off", showBackground = true)
@Preview(
    name = "Dark Mode--Off",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmBadgeOffPreview() {
    AlarmMeThemeProvider {
        AmBadge(
            modifier = Modifier.padding(10.dp),
            text = "OFF",
            enabled = false
        )
    }
}

@Preview(name = "Light Mode--Normal", showBackground = true)
@Preview(
    name = "Dark Mode--Normal",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmBadgeNormalPreview() {
    AlarmMeThemeProvider {
        AmBadge(
            modifier = Modifier.padding(10.dp),
            text = "ONE-OFF"
        )
    }
}

@Preview(name = "Light Mode--Highlighted", showBackground = true)
@Preview(
    name = "Dark Mode--Highlighted",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmBadgeHighlightedPreview() {
    AlarmMeThemeProvider {
        AmBadge(
            modifier = Modifier.padding(10.dp),
            text = "PLAYING",
            highlighted = true
        )
    }
}
