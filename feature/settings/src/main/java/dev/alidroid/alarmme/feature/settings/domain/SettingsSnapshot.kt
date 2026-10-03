package dev.alidroid.alarmme.feature.settings.domain

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.ThemePreferencePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences

data class SettingsSnapshot(
    val ringtone: RingtonePreferences,
    val snooze: SnoozePreferences,
    val gradualVolume: GradualVolumePreferences,
    val vibration: VibrationPreferences,
    val alarmTimeout: AlarmTimeoutPreferences,
    val volumeButtonsAction: VolumeButtonsActionPreferences,
    val twentyFourHourTime: TwentyFourHourTimePreferences,
    val themePreference: ThemePreferencePreferences,
)
