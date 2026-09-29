package com.muhammadali.alarmme.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.settings.presentation.component.SettingsTopBar
import com.muhammadali.alarmme.ui.component.AmDisclosureRow
import com.muhammadali.alarmme.ui.component.AmLabeledSectionCard
import com.muhammadali.alarmme.ui.component.AmNavigationDisclosureRow
import com.muhammadali.alarmme.ui.component.AmSectionLabel
import com.muhammadali.alarmme.ui.component.AmSectionedCard
import com.muhammadali.alarmme.ui.component.AmToggleDisclosureRow
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    soundLabel: String,
    onSoundClick: () -> Unit,
    snoozeLabel: String,
    onSnoozeClick: () -> Unit,
    gradualVolumeEnabled: Boolean,
    onGradualVolumeToggle: (Boolean) -> Unit,
    vibrateEnabled: Boolean,
    onVibrateToggle: (Boolean) -> Unit,
    silenceAfterLabel: String,
    onSilenceAfterClick: () -> Unit,
    volumeButtonsLabel: String,
    onVolumeButtonsClick: () -> Unit,
    is24HourTimeEnabled: Boolean,
    on24HourTimeToggle: (Boolean) -> Unit,
    weekStartsOnLabel: String,
    onWeekStartsOnClick: () -> Unit,
    themeLabel: String,
    onThemeClick: () -> Unit,
    versionLabel: String,
    onSourceCodeClick: () -> Unit,
    onLicencesClick: () -> Unit,
    showDeveloperSection: Boolean,
    onDebugPanelClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        SettingsTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DefaultAlarmPreferencesHeader(
                description = "Used for new alarms. Alarms you've already set keep their own settings."
            )
            AmSectionedCard {
                sections(count = 4) { index ->
                    when (index) {
                        0 -> AmNavigationDisclosureRow(
                            text = "Sound",
                            value = soundLabel,
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

                        else -> AmToggleDisclosureRow(
                            text = "Vibrate",
                            checked = vibrateEnabled,
                            onCheckedChange = onVibrateToggle
                        )
                    }
                }
            }

            AmLabeledSectionCard(label = "Ringing") {
                sections(count = 2) { index ->
                    when (index) {
                        0 -> AmNavigationDisclosureRow(
                            text = "Silence after",
                            value = silenceAfterLabel,
                            onClick = onSilenceAfterClick
                        )

                        else -> AmNavigationDisclosureRow(
                            text = "Volume buttons",
                            value = volumeButtonsLabel,
                            onClick = onVolumeButtonsClick
                        )
                    }
                }
            }

            AmLabeledSectionCard(label = "General") {
                sections(count = 3) { index ->
                    when (index) {
                        0 -> AmToggleDisclosureRow(
                            text = "24-hour time",
                            checked = is24HourTimeEnabled,
                            onCheckedChange = on24HourTimeToggle
                        )

                        1 -> AmNavigationDisclosureRow(
                            text = "Week starts on",
                            value = weekStartsOnLabel,
                            onClick = onWeekStartsOnClick
                        )

                        else -> AmNavigationDisclosureRow(
                            text = "Theme",
                            value = themeLabel,
                            onClick = onThemeClick
                        )
                    }
                }
            }

            AmLabeledSectionCard(label = "About") {
                sections(count = 3) { index ->
                    when (index) {
                        0 -> AmDisclosureRow(
                            text = "Version",
                            onClick = null
                        ) {
                            Text(
                                text = versionLabel,
                                style = LocalAlarmMeTypography.current.labelMedium,
                                color = colors.onSurfaceVariant
                            )
                        }

                        1 -> AmDisclosureRow(
                            text = "Source code",
                            onClick = onSourceCodeClick
                        ) {
                            Text(
                                text = "↗",
                                style = LocalAlarmMeTypography.current.bodyLarge,
                                color = colors.onSurfaceVariant
                            )
                        }

                        else -> AmNavigationDisclosureRow(
                            text = "Open-source licences",
                            onClick = onLicencesClick
                        )
                    }
                }
            }

            if (showDeveloperSection) {
                AmLabeledSectionCard(label = "Developer", labelColor = colors.primary) {
                    sections(count = 1) {
                        AmNavigationDisclosureRow(
                            text = "Debug panel",
                            onClick = onDebugPanelClick
                        )
                    }
                }
            }
        }
    }
}

/** Section header with a supporting description line, used only for the one section that needs it. */
@Composable
private fun DefaultAlarmPreferencesHeader(
    description: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current

    Column(
        modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        AmSectionLabel(text = "Default alarm preferences", color = colors.primary)
        Text(
            text = description,
            style = LocalAlarmMeTypography.current.bodySmall,
            color = colors.onSurfaceVariant
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun SettingsScreenContentPreview() {
    var gradualVolumeEnabled by remember { mutableStateOf(true) }
    var vibrateEnabled by remember { mutableStateOf(false) }
    var is24HourTimeEnabled by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        SettingsScreenContent(
            soundLabel = "Sunrise chime",
            onSoundClick = {},
            snoozeLabel = "9 min · 3 times",
            onSnoozeClick = {},
            gradualVolumeEnabled = gradualVolumeEnabled,
            onGradualVolumeToggle = { gradualVolumeEnabled = it },
            vibrateEnabled = vibrateEnabled,
            onVibrateToggle = { vibrateEnabled = it },
            silenceAfterLabel = "10 min",
            onSilenceAfterClick = {},
            volumeButtonsLabel = "Snooze",
            onVolumeButtonsClick = {},
            is24HourTimeEnabled = is24HourTimeEnabled,
            on24HourTimeToggle = { is24HourTimeEnabled = it },
            weekStartsOnLabel = "Monday",
            onWeekStartsOnClick = {},
            themeLabel = "System",
            onThemeClick = {},
            versionLabel = "1.0.0",
            onSourceCodeClick = {},
            onLicencesClick = {},
            showDeveloperSection = false,
            onDebugPanelClick = {},
            onBackClick = {}
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun SettingsScreenContentDebugBuildPreview() {
    var gradualVolumeEnabled by remember { mutableStateOf(true) }
    var vibrateEnabled by remember { mutableStateOf(false) }
    var is24HourTimeEnabled by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        SettingsScreenContent(
            soundLabel = "Sunrise chime",
            onSoundClick = {},
            snoozeLabel = "9 min · 3 times",
            onSnoozeClick = {},
            gradualVolumeEnabled = gradualVolumeEnabled,
            onGradualVolumeToggle = { gradualVolumeEnabled = it },
            vibrateEnabled = vibrateEnabled,
            onVibrateToggle = { vibrateEnabled = it },
            silenceAfterLabel = "10 min",
            onSilenceAfterClick = {},
            volumeButtonsLabel = "Snooze",
            onVolumeButtonsClick = {},
            is24HourTimeEnabled = is24HourTimeEnabled,
            on24HourTimeToggle = { is24HourTimeEnabled = it },
            weekStartsOnLabel = "Monday",
            onWeekStartsOnClick = {},
            themeLabel = "System",
            onThemeClick = {},
            versionLabel = "1.0.0-debug (142)",
            onSourceCodeClick = {},
            onLicencesClick = {},
            showDeveloperSection = true,
            onDebugPanelClick = {},
            onBackClick = {}
        )
    }
}
