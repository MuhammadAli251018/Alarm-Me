package com.muhammadali.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.muhammadali.alarmme.ui.component.AmNavigationDisclosureRow
import com.muhammadali.alarmme.ui.component.AmSectionedCard
import com.muhammadali.alarmme.ui.component.AmToggleDisclosureRow
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors


@Composable
fun AlarmSettingsDetails(
    modifier: Modifier = Modifier,
    soundLabel: String,
    snoozeLabel: String,
    gradualVolumeEnabled: Boolean,
    vibrateEnabled: Boolean,
    alarmLabel: String,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current

    AmSectionedCard(modifier = modifier.fillMaxWidth()) {
        sections(count = 5) { index ->
            when (index) {
                0 -> AmNavigationDisclosureRow(
                    text = "Sound",
                    value = soundLabel,
                    valueColor = colors.primary,
                    onClick = onSoundClick
                )

                1 -> AmNavigationDisclosureRow(
                    text = "Snooze",
                    value = snoozeLabel,
                    onClick = onSnoozeClick
                )

                2 -> AmToggleDisclosureRow(
                    text = "Gradual volume",
                    checked = gradualVolumeEnabled,
                    onCheckedChange = onGradualVolumeToggle
                )

                3 -> AmToggleDisclosureRow(
                    text = "Vibrate",
                    checked = vibrateEnabled,
                    onCheckedChange = onVibrateToggle
                )

                else -> AmNavigationDisclosureRow(
                    text = "Label",
                    value = alarmLabel,
                    onClick = onLabelClick
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmSettingsDetailsPreview() {
    AlarmMeThemeProvider {
        AlarmSettingsDetails(
            soundLabel = "Sunrise chime",
            snoozeLabel = "9 min · 3 times",
            gradualVolumeEnabled = true,
            vibrateEnabled = false,
            alarmLabel = "Weekday mornings",
            onSoundClick = {},
            onSnoozeClick = {},
            onGradualVolumeToggle = {},
            onVibrateToggle = {},
            onLabelClick = {}
        )
    }
}
