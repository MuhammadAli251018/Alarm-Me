package com.muhammadali.alarmme.feature.settings.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.appSettings.GradualVolumePreferences
import com.muhammadali.alarmme.appSettings.TwentyFourHourTimePreferences
import com.muhammadali.alarmme.appSettings.VibrationPreferences
import com.muhammadali.alarmme.feature.settings.domain.AppVersionInfoUseCase
import com.muhammadali.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase
import com.muhammadali.alarmme.feature.settings.domain.SettingsReadUseCase
import com.muhammadali.alarmme.feature.settings.domain.SettingsUpdateUseCase
import com.muhammadali.alarmme.feature.settings.presentation.model.SettingsEvent
import com.muhammadali.alarmme.feature.settings.presentation.model.SettingsState
import com.muhammadali.alarmme.feature.settings.presentation.model.toLoadedState
import com.muhammadali.alarmme.mvi.BaseScreenModel
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

            SettingsEvent.OnRetry -> loadSettings()

            // No destination exists yet for any of these — dispatch is wired, the
            // navigation/action itself is a follow-up once those screens exist.
            SettingsEvent.OnSoundClick,
            SettingsEvent.OnSnoozeClick,
            SettingsEvent.OnSilenceAfterClick,
            SettingsEvent.OnVolumeButtonsClick,
            SettingsEvent.OnThemeClick,
            SettingsEvent.OnSourceCodeClick,
            SettingsEvent.OnLicencesClick,
            SettingsEvent.OnDebugPanelClick -> Unit
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
                        appVersionInfo = appVersionInfo
                    )
                }
                .catch { _state.value = SettingsState.Error }
                .collect { loaded -> _state.value = loaded }
        }
    }
}
