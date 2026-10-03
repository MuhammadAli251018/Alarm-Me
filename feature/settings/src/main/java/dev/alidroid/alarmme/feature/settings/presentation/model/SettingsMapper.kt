package dev.alidroid.alarmme.feature.settings.presentation.model

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfo
import dev.alidroid.alarmme.feature.settings.domain.SettingsSnapshot

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
