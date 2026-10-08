package dev.alidroid.alarmme.feature.settings.presentation

import android.os.Parcelable
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
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.alidroid.alarmme.feature.settings.presentation.component.SettingsTopBar
import dev.alidroid.alarmme.navigation.NavDestination
import dev.alidroid.alarmme.navigation.navigateTo
import dev.alidroid.alarmme.feature.settings.presentation.model.SettingsEvent
import dev.alidroid.alarmme.feature.settings.presentation.model.SettingsState
import dev.alidroid.alarmme.navigation.LocalNavGraph
import dev.alidroid.alarmme.ui.component.AmDisclosureRow
import dev.alidroid.alarmme.ui.component.AmLabeledSectionCard
import dev.alidroid.alarmme.ui.component.AmNavigationDisclosureRow
import dev.alidroid.alarmme.ui.component.AmSectionLabel
import dev.alidroid.alarmme.ui.component.AmSectionedCard
import dev.alidroid.alarmme.ui.component.AmToggleDisclosureRow
import dev.alidroid.alarmme.ui.component.ErrorScreenContent
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerDialog
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerOptions
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration

@Parcelize
class SettingsScreen(private val debugPanelDestination: NavDestination) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val screenModel: SettingsScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current
        val navGraph = LocalNavGraph.current

        SettingsScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize(),
            state = state,
            onBackClick = { navigator?.pop() },
            onRetry = { dispatch(SettingsEvent.OnRetry) },
            onSoundClick = { dispatch(SettingsEvent.OnSoundClick) },
            onSnoozeChanged = { lengthIndex, limitIndex -> dispatch(SettingsEvent.OnSnoozeChanged(lengthIndex, limitIndex)) },
            onGradualVolumeToggle = { dispatch(SettingsEvent.OnGradualVolumeToggle(it)) },
            onVibrateToggle = { dispatch(SettingsEvent.OnVibrateToggle(it)) },
            onSilenceAfterClick = { dispatch(SettingsEvent.OnSilenceAfterClick) },
            onVolumeButtonsClick = { dispatch(SettingsEvent.OnVolumeButtonsClick) },
            on24HourTimeToggle = { dispatch(SettingsEvent.OnTwentyFourHourTimeToggle(it)) },
            onThemeClick = { dispatch(SettingsEvent.OnThemeClick) },
            onSourceCodeClick = { dispatch(SettingsEvent.OnSourceCodeClick) },
            onLicencesClick = { dispatch(SettingsEvent.OnLicencesClick) },
            onDebugPanelClick = { navigator?.navigateTo(debugPanelDestination, navGraph) }
        )
    }
}

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeChanged: (Int?, Int?) -> Unit,
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
            snoozeLengthIndex = state.snoozeLengthIndex,
            snoozeLimitIndex = state.snoozeLimitIndex,
            lengthOptions = state.lengthOptions,
            limitOptions = state.limitOptions,
            snoozeLabel = state.snoozeLabel,
            onSnoozeChanged = onSnoozeChanged,
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
    snoozeLengthIndex: Int?,
    snoozeLimitIndex: Int?,
    snoozeLabel: String,
    onSnoozeChanged: (Int?, Int?) -> Unit,
    lengthOptions: List<Duration> = SnoozePickerOptions.Defaults.snoozeLengthOptions,
    limitOptions: List<Int?> = SnoozePickerOptions.Defaults.snoozeLimitOptions,
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
    var showSnoozePicker by rememberSaveable { mutableStateOf(false) }

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
                            onClick = { showSnoozePicker = true }
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

    if (showSnoozePicker) {
        var draftLengthIndex by remember { mutableStateOf(snoozeLengthIndex) }
        var draftLimitIndex by remember { mutableStateOf(snoozeLimitIndex) }
        val defaultLimitIndex = remember(limitOptions) { limitOptions.indexOf(3) }

        SnoozePickerDialog(
            title = "Default snooze",
            subtitle = "New alarms start with this. Existing alarms don't change.",
            snoozeLengthOptions = lengthOptions,
            snoozeLimitOptions = limitOptions,
            selectedLengthIndex = draftLengthIndex,
            selectedLimitIndex = draftLimitIndex,
            onLengthIndexSelected = { index ->
                draftLengthIndex = index
                if (draftLimitIndex == null) draftLimitIndex = defaultLimitIndex
            },
            onLimitIndexSelected = { draftLimitIndex = it },
            onTurnOff = {
                onSnoozeChanged(null, null)
                showSnoozePicker = false
            },
            onSave = {
                onSnoozeChanged(draftLengthIndex, draftLimitIndex ?: defaultLimitIndex)
                showSnoozePicker = false
            },
            onDismiss = { showSnoozePicker = false }
        )
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
            snoozeLengthIndex = 1,
            snoozeLimitIndex = 2,
            snoozeLabel = "9 min · 3 times",
            onSnoozeChanged = { _, _ -> },
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
            snoozeLengthIndex = 1,
            snoozeLimitIndex = 2,
            snoozeLabel = "9 min · 3 times",
            onSnoozeChanged = { _, _ -> },
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
