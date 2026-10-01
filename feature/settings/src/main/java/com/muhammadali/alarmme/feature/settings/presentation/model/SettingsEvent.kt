package com.muhammadali.alarmme.feature.settings.presentation.model

import com.muhammadali.alarmme.mvi.UiEvent

sealed interface SettingsEvent : UiEvent {
    data class OnGradualVolumeToggle(val enabled: Boolean) : SettingsEvent
    data class OnVibrateToggle(val enabled: Boolean) : SettingsEvent
    data class OnTwentyFourHourTimeToggle(val enabled: Boolean) : SettingsEvent

    /** Retry after the settings flow failed to load. */
    data object OnRetry : SettingsEvent

    // Rows that need a picker/destination that doesn't exist yet — dispatched so the
    // wiring is in place, but currently no-ops in the ScreenModel.
    data object OnSoundClick : SettingsEvent
    data object OnSnoozeClick : SettingsEvent
    data object OnSilenceAfterClick : SettingsEvent
    data object OnVolumeButtonsClick : SettingsEvent
    data object OnThemeClick : SettingsEvent
    data object OnSourceCodeClick : SettingsEvent
    data object OnLicencesClick : SettingsEvent
}
