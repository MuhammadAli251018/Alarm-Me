package dev.alidroid.alarmme.feature.settings.domain

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.ThemePreferencePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences

interface SettingsUpdateUseCase {
    suspend fun setRingtone(value: RingtonePreferences)
    suspend fun setSnooze(value: SnoozePreferences)
    suspend fun setGradualVolume(value: GradualVolumePreferences)
    suspend fun setVibration(value: VibrationPreferences)
    suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences)
    suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences)
    suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences)
    suspend fun setThemePreference(value: ThemePreferencePreferences)
}
