package com.muhammadali.alarmme.feature.settings.data

import com.muhammadali.alarmme.appSettings.AlarmTimeoutPreferences
import com.muhammadali.alarmme.appSettings.AppSettings
import com.muhammadali.alarmme.appSettings.GradualVolumePreferences
import com.muhammadali.alarmme.appSettings.RingtonePreferences
import com.muhammadali.alarmme.appSettings.SnoozePreferences
import com.muhammadali.alarmme.appSettings.ThemePreferencePreferences
import com.muhammadali.alarmme.appSettings.TwentyFourHourTimePreferences
import com.muhammadali.alarmme.appSettings.VibrationPreferences
import com.muhammadali.alarmme.appSettings.VolumeButtonsActionPreferences
import com.muhammadali.alarmme.appSettings.key
import com.muhammadali.alarmme.feature.settings.domain.SettingsUpdateUseCase
import com.muhammadali.alarmme.preferences.PreferencesRepository
import com.muhammadali.alarmme.preferences.setPrefValue

class SettingsUpdateUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : SettingsUpdateUseCase {

    override suspend fun setRingtone(value: RingtonePreferences) =
        preferencesRepository.setPrefValue(AppSettings.Ringtone.key, value)

    override suspend fun setSnooze(value: SnoozePreferences) =
        preferencesRepository.setPrefValue(AppSettings.Snooze.key, value)

    override suspend fun setGradualVolume(value: GradualVolumePreferences) =
        preferencesRepository.setPrefValue(AppSettings.GradualVolume.key, value)

    override suspend fun setVibration(value: VibrationPreferences) =
        preferencesRepository.setPrefValue(AppSettings.Vibration.key, value)

    override suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences) =
        preferencesRepository.setPrefValue(AppSettings.AlarmTimeout.key, value)

    override suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences) =
        preferencesRepository.setPrefValue(AppSettings.VolumeButtonsAction.key, value)

    override suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences) =
        preferencesRepository.setPrefValue(AppSettings.TwentyFourHourTime.key, value)

    override suspend fun setThemePreference(value: ThemePreferencePreferences) =
        preferencesRepository.setPrefValue(AppSettings.ThemePreference.key, value)
}
