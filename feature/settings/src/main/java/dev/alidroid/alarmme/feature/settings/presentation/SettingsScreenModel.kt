package dev.alidroid.alarmme.feature.settings.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfoUseCase
import dev.alidroid.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsReadUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase
import dev.alidroid.alarmme.feature.settings.presentation.model.SettingsEvent
import dev.alidroid.alarmme.feature.settings.presentation.model.SettingsState
import dev.alidroid.alarmme.feature.settings.presentation.model.snoozePreferencesOf
import dev.alidroid.alarmme.feature.settings.presentation.model.toLoadedState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SettingsScreenModel(
    private val settingsReadUseCase: SettingsReadUseCase,
    private val settingsUpdateUseCase: SettingsUpdateUseCase,
    private val resolveRingtoneTitleUseCase: ResolveRingtoneTitleUseCase,
    private val appVersionInfoUseCase: AppVersionInfoUseCase,
) : BaseScreenModel<SettingsState, SettingsEvent, Nothing>(
    initialState = SettingsState.Initial
) {

    private var observeJob: Job? = null
    private val snoozeLengthOptions = SnoozePickerOptions.Defaults.snoozeLengthOptions
    private val snoozeLimitOptions = SnoozePickerOptions.Defaults.snoozeLimitOptions

    init { loadSettings() }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnGradualVolumeToggle -> screenModelScope.launch {
                settingsUpdateUseCase.setGradualVolume(
                    if (event.enabled) GradualVolumePreferences.Enabled else GradualVolumePreferences.Disabled
                )
            }

            is SettingsEvent.OnVibrateToggle -> screenModelScope.launch {
                settingsUpdateUseCase.setVibration(
                    if (event.enabled) VibrationPreferences.Enabled else VibrationPreferences.Disabled
                )
            }

            is SettingsEvent.OnTwentyFourHourTimeToggle -> screenModelScope.launch {
                settingsUpdateUseCase.setTwentyFourHourTime(
                    if (event.enabled) TwentyFourHourTimePreferences.Enabled else TwentyFourHourTimePreferences.Disabled
                )
            }

            is SettingsEvent.OnSnoozeChanged -> screenModelScope.launch {
                settingsUpdateUseCase.setSnooze(
                    snoozePreferencesOf(event.lengthIndex, event.limitIndex, snoozeLengthOptions, snoozeLimitOptions)
                )
            }

            is SettingsEvent.OnRingtoneUpdated -> screenModelScope.launch {
                settingsUpdateUseCase.setRingtone(RingtonePreferences.Ringtone(event.ringtone.id.value))
            }

            SettingsEvent.OnRetry -> loadSettings()

            // No destination exists yet for any of these — dispatch is wired, the
            // navigation/action itself is a follow-up once those screens exist.
            SettingsEvent.OnSilenceAfterClick,
            SettingsEvent.OnVolumeButtonsClick,
            SettingsEvent.OnThemeClick,
            SettingsEvent.OnSourceCodeClick,
            SettingsEvent.OnLicencesClick -> Unit
        }
    }

    private fun loadSettings() {
        observeJob?.cancel()
        _state.value = SettingsState.Loading

        observeJob = screenModelScope.launch {
            val appVersionInfo = appVersionInfoUseCase.getAppVersionInfo()

            settingsReadUseCase.observeSettings()
                .onStart { delay(1.seconds) }
                .map { snapshot ->
                    snapshot.toLoadedState(
                        ringtoneTitle = resolveRingtoneTitleUseCase.resolveTitle(snapshot.ringtone),
                        appVersionInfo = appVersionInfo,
                        lengthOptions = snoozeLengthOptions,
                        limitOptions = snoozeLimitOptions
                    )
                }
                .catch { _state.value = SettingsState.Error }
                .collect { loaded -> _state.value = loaded }
        }
    }
}
