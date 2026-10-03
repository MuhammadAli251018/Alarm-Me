package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.AppSettings
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.ThemePreferencePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences
import dev.alidroid.alarmme.appSettings.key
import dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase
import dev.alidroid.alarmme.preferences.PreferencesRepository
import dev.alidroid.alarmme.preferences.setPrefValue

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
