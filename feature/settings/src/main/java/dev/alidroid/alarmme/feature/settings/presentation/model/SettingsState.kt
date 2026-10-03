package dev.alidroid.alarmme.feature.settings.presentation.model

import dev.alidroid.alarmme.mvi.UiState

sealed interface SettingsState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : SettingsState

    data class Loaded(
        val soundLabel: String,
        val snoozeLabel: String,
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
