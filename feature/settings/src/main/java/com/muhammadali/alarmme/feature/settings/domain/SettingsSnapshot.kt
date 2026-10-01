package com.muhammadali.alarmme.feature.settings.domain

import com.muhammadali.alarmme.appSettings.AlarmTimeoutPreferences
import com.muhammadali.alarmme.appSettings.GradualVolumePreferences
import com.muhammadali.alarmme.appSettings.RingtonePreferences
import com.muhammadali.alarmme.appSettings.SnoozePreferences
import com.muhammadali.alarmme.appSettings.ThemePreferencePreferences
import com.muhammadali.alarmme.appSettings.TwentyFourHourTimePreferences
import com.muhammadali.alarmme.appSettings.VibrationPreferences
import com.muhammadali.alarmme.appSettings.VolumeButtonsActionPreferences

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
