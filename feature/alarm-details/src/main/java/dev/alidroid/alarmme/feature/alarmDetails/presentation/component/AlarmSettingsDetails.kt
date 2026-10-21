package dev.alidroid.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.alidroid.alarmme.feature.alarmDetails.R
import dev.alidroid.alarmme.ui.component.AmNavigationDisclosureRow
import dev.alidroid.alarmme.ui.component.AmSectionedCard
import dev.alidroid.alarmme.ui.component.AmToggleDisclosureRow
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors


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
                    text = stringResource(R.string.alarm_settings_sound),
                    value = soundLabel,
                    valueColor = colors.primary,
                    onClick = onSoundClick
                )

                1 -> AmNavigationDisclosureRow(
                    text = stringResource(R.string.alarm_settings_snooze),
                    value = snoozeLabel,
                    onClick = onSnoozeClick
                )

                2 -> AmToggleDisclosureRow(
                    text = stringResource(R.string.alarm_settings_gradual_volume),
                    checked = gradualVolumeEnabled,
                    onCheckedChange = onGradualVolumeToggle
                )

                3 -> AmToggleDisclosureRow(
                    text = stringResource(R.string.alarm_settings_vibrate),
                    checked = vibrateEnabled,
                    onCheckedChange = onVibrateToggle
                )

                else -> AmNavigationDisclosureRow(
                    text = stringResource(R.string.alarm_settings_label),
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
