package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

@Composable
fun AmStatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    isFilled: Boolean = false,
    modifier: Modifier = Modifier
) {
    BaseBadge(
        modifier = modifier,
        text = text,
        containerColor = if (isFilled) containerColor else Color.Transparent,
        contentColor = contentColor,
        borderColor = if (isFilled) Color.Transparent else borderColor
    )
}

@Preview(name = "Light Mode--Off", showBackground = true)
@Preview(
    name = "Dark Mode--Off",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmStatusBadgeOffPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmStatusBadge(
            modifier = Modifier.padding(10.dp),
            text = "OFF",
            containerColor = colors.outlineVariant,
            contentColor = colors.onSurfaceVariant,
            borderColor = colors.outlineVariant
        )
    }
}

@Preview(name = "Light Mode--One Off", showBackground = true)
@Preview(
    name = "Dark Mode--One Off",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmStatusBadgeOneOffPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmStatusBadge(
            modifier = Modifier.padding(10.dp),
            text = "ONE-OFF",
            containerColor = colors.primaryContainer,
            contentColor = colors.primary,
            borderColor = colors.primaryContainer
        )
    }
}

@Preview(name = "Light Mode--Playing", showBackground = true)
@Preview(
    name = "Dark Mode--Playing",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmStatusBadgePlayingPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmStatusBadge(
            modifier = Modifier.padding(10.dp),
            text = "PLAYING",
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer,
            borderColor = colors.primaryContainer,
            isFilled = true
        )
    }
}

@Preview(name = "Light Mode--Unavailable", showBackground = true)
@Preview(
    name = "Dark Mode--Unavailable",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmStatusBadgeUnavailablePreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmStatusBadge(
            modifier = Modifier.padding(10.dp),
            text = "UNAVAILABLE",
            containerColor = colors.primaryContainer,
            contentColor = colors.error,
            borderColor = colors.primaryContainer
        )
    }
}
