package dev.alidroid.alarmme.feature.settings.presentation.model

import dev.alidroid.alarmme.mvi.UiState
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerOptions
import kotlin.time.Duration

sealed interface SettingsState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : SettingsState

    data class Loaded(
        val soundLabel: String,
        val snoozeLengthIndex: Int?,
        val snoozeLimitIndex: Int?,
        val snoozeLabel: String,
        val lengthOptions: List<Duration> = SnoozePickerOptions.Defaults.snoozeLengthOptions,
        val limitOptions: List<Int?> = SnoozePickerOptions.Defaults.snoozeLimitOptions,
        val gradualVolumeEnabled: Boolean,
        val vibrateEnabled: Boolean,
        val silenceAfterLabel: String,
        val volumeButtonsLabel: String,
        val is24HourTimeEnabled: Boolean,
        val themeLabel: String,
        val versionLabel: String,
        val showDeveloperSection: Boolean,
    ) : SettingsState

    data object Error : SettingsState
}
