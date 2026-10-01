package com.muhammadali.alarmme.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.muhammadali.alarmme.feature.settings.presentation.component.SettingsTopBar
import com.muhammadali.alarmme.feature.settings.presentation.model.SettingsEvent
import com.muhammadali.alarmme.feature.settings.presentation.model.SettingsState
import com.muhammadali.alarmme.ui.component.AmDisclosureRow
import com.muhammadali.alarmme.ui.component.AmLabeledSectionCard
import com.muhammadali.alarmme.ui.component.AmNavigationDisclosureRow
import com.muhammadali.alarmme.ui.component.AmSectionLabel
import com.muhammadali.alarmme.ui.component.AmSectionedCard
import com.muhammadali.alarmme.ui.component.AmToggleDisclosureRow
import com.muhammadali.alarmme.ui.component.ErrorScreenContent
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: SettingsScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current

        SettingsScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize(),
            state = state,
            onBackClick = { navigator?.pop() },
            onRetry = { dispatch(SettingsEvent.OnRetry) },
            onSoundClick = { dispatch(SettingsEvent.OnSoundClick) },
            onSnoozeClick = { dispatch(SettingsEvent.OnSnoozeClick) },
            onGradualVolumeToggle = { dispatch(SettingsEvent.OnGradualVolumeToggle(it)) },
            onVibrateToggle = { dispatch(SettingsEvent.OnVibrateToggle(it)) },
            onSilenceAfterClick = { dispatch(SettingsEvent.OnSilenceAfterClick) },
            onVolumeButtonsClick = { dispatch(SettingsEvent.OnVolumeButtonsClick) },
            on24HourTimeToggle = { dispatch(SettingsEvent.OnTwentyFourHourTimeToggle(it)) },
            onThemeClick = { dispatch(SettingsEvent.OnThemeClick) },
            onSourceCodeClick = { dispatch(SettingsEvent.OnSourceCodeClick) },
            onLicencesClick = { dispatch(SettingsEvent.OnLicencesClick) },
            onDebugPanelClick = { dispatch(SettingsEvent.OnDebugPanelClick) }
        )
    }
}

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onSilenceAfterClick: () -> Unit,
    onVolumeButtonsClick: () -> Unit,
    on24HourTimeToggle: (Boolean) -> Unit,
    onThemeClick: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onLicencesClick: () -> Unit,
    onDebugPanelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        SettingsState.Loading -> Column(
            modifier = modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            SettingsTopBar(onBackClick = onBackClick)
        }

        SettingsState.Error -> ErrorScreenContent(
            modifier = modifier.fillMaxSize(),
            errorHeading = "Couldn't load settings",
            errorDescription = "Something went wrong reading your settings.",
            onRetry = onRetry,
            onDismiss = onBackClick
        )

        is SettingsState.Loaded -> SettingsScreenContent(
            modifier = modifier.fillMaxSize(),
            soundLabel = state.soundLabel,
            onSoundClick = onSoundClick,
            snoozeLabel = state.snoozeLabel,
            onSnoozeClick = onSnoozeClick,
            gradualVolumeEnabled = state.gradualVolumeEnabled,
            onGradualVolumeToggle = onGradualVolumeToggle,
            vibrateEnabled = state.vibrateEnabled,
            onVibrateToggle = onVibrateToggle,
            silenceAfterLabel = state.silenceAfterLabel,
            onSilenceAfterClick = onSilenceAfterClick,
            volumeButtonsLabel = state.volumeButtonsLabel,
            onVolumeButtonsClick = onVolumeButtonsClick,
            is24HourTimeEnabled = state.is24HourTimeEnabled,
            on24HourTimeToggle = on24HourTimeToggle,
            themeLabel = state.themeLabel,
            onThemeClick = onThemeClick,
            versionLabel = state.versionLabel,
            onSourceCodeClick = onSourceCodeClick,
            onLicencesClick = onLicencesClick,
            showDeveloperSection = state.showDeveloperSection,
            onDebugPanelClick = onDebugPanelClick,
            onBackClick = onBackClick
        )
    }
}

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
                            onClick = onSnoozeClick,
                            enabled = false // TODO: Enable this
                        )

                        2 -> AmToggleDisclosureRow(
                            text = "Gradual volume",
                            checked = gradualVolumeEnabled,
                            onCheckedChange = onGradualVolumeToggle,
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
                            onClick = onSilenceAfterClick,
                            enabled = false // TODO: Enable this
                        )

                        else -> AmNavigationDisclosureRow(
                            text = "Volume buttons",
                            value = volumeButtonsLabel,
                            onClick = onVolumeButtonsClick,
                            enabled = false // TODO: Enable this
                        )
                    }
                }
            }

            AmLabeledSectionCard(label = "General") {
                sections(count = 2) { index ->
                    when (index) {
                        0 -> AmToggleDisclosureRow(
                            text = "24-hour time",
                            checked = is24HourTimeEnabled,
                            onCheckedChange = on24HourTimeToggle,
                            enabled = false // TODO: Enable this
                        )

                        else -> AmNavigationDisclosureRow(
                            text = "Theme",
                            value = themeLabel,
                            onClick = onThemeClick,
                            enabled = false // TODO: Enable this
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
