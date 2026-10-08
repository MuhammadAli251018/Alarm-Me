package dev.alidroid.alarmme.feature.settings.presentation.model

import dev.alidroid.alarmme.mvi.UiEvent

sealed interface SettingsEvent : UiEvent {
    data class OnGradualVolumeToggle(val enabled: Boolean) : SettingsEvent
    data class OnVibrateToggle(val enabled: Boolean) : SettingsEvent
    data class OnTwentyFourHourTimeToggle(val enabled: Boolean) : SettingsEvent

    /** The snooze dialog's Save (or Turn off) was tapped with this selection. */
    data class OnSnoozeChanged(val lengthIndex: Int?, val limitIndex: Int?) : SettingsEvent

    /** Retry after the settings flow failed to load. */
    data object OnRetry : SettingsEvent

    // Rows that need a picker/destination that doesn't exist yet — dispatched so the
    // wiring is in place, but currently no-ops in the ScreenModel.
    data object OnSoundClick : SettingsEvent
    data object OnSilenceAfterClick : SettingsEvent
    data object OnVolumeButtonsClick : SettingsEvent
    data object OnThemeClick : SettingsEvent
    data object OnSourceCodeClick : SettingsEvent
    data object OnLicencesClick : SettingsEvent
}
