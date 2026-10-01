package com.muhammadali.alarmme.feature.settings.presentation.model

import com.muhammadali.alarmme.appSettings.AlarmTimeoutPreferences
import com.muhammadali.alarmme.appSettings.GradualVolumePreferences
import com.muhammadali.alarmme.appSettings.SnoozePreferences
import com.muhammadali.alarmme.appSettings.TwentyFourHourTimePreferences
import com.muhammadali.alarmme.appSettings.VibrationPreferences
import com.muhammadali.alarmme.appSettings.VolumeButtonsActionPreferences
import com.muhammadali.alarmme.feature.settings.domain.AppVersionInfo
import com.muhammadali.alarmme.feature.settings.domain.SettingsSnapshot

/**
 * The domain → state boundary for the settings screen.
 */
internal fun SettingsSnapshot.toLoadedState(
    ringtoneTitle: String,
    appVersionInfo: AppVersionInfo,
): SettingsState.Loaded = SettingsState.Loaded(
    soundLabel = ringtoneTitle,
    snoozeLabel = snooze.toLabel(),
    gradualVolumeEnabled = gradualVolume == GradualVolumePreferences.Enabled,
    vibrateEnabled = vibration == VibrationPreferences.Enabled,
    silenceAfterLabel = alarmTimeout.toLabel(),
    volumeButtonsLabel = volumeButtonsAction.toLabel(),
    is24HourTimeEnabled = twentyFourHourTime == TwentyFourHourTimePreferences.Enabled,
    themeLabel = themePreference.name,
    versionLabel = appVersionInfo.versionLabel,
    showDeveloperSection = appVersionInfo.showDeveloperSection,
)

private fun SnoozePreferences.toLabel(): String = when (this) {
    SnoozePreferences.NoSnooze -> "No snooze"
    is SnoozePreferences.SnoozeDuration -> "${duration.inWholeMinutes} min · $repeats times"
}

private fun AlarmTimeoutPreferences.toLabel(): String = when (this) {
    AlarmTimeoutPreferences.NoTimeout -> "No timeout"
    is AlarmTimeoutPreferences.TimeoutDuration -> "${duration.inWholeMinutes} min"
}

/** "Mute" means the alarm snoozes, not that it goes silent — labelled accordingly. */
private fun VolumeButtonsActionPreferences.toLabel(): String = when (this) {
    VolumeButtonsActionPreferences.Nothing -> "Nothing"
    VolumeButtonsActionPreferences.Mute -> "Snooze"
    VolumeButtonsActionPreferences.VolumeUpMutes -> "Volume up · Snooze"
    VolumeButtonsActionPreferences.VolumeDownMutes -> "Volume down · Snooze"
}
