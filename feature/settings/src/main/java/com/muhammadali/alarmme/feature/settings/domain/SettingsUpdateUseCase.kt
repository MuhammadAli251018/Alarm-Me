package com.muhammadali.alarmme.feature.settings.domain

import com.muhammadali.alarmme.appSettings.AlarmTimeoutPreferences
import com.muhammadali.alarmme.appSettings.GradualVolumePreferences
import com.muhammadali.alarmme.appSettings.RingtonePreferences
import com.muhammadali.alarmme.appSettings.SnoozePreferences
import com.muhammadali.alarmme.appSettings.ThemePreferencePreferences
import com.muhammadali.alarmme.appSettings.TwentyFourHourTimePreferences
import com.muhammadali.alarmme.appSettings.VibrationPreferences
import com.muhammadali.alarmme.appSettings.VolumeButtonsActionPreferences

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
